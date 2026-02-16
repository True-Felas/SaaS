package com.Martin.SaaS.service;

import com.Martin.SaaS.model.*;
import com.Martin.SaaS.model.enums.*;
import com.Martin.SaaS.repository.FacturaRepository;
import com.Martin.SaaS.repository.PlanRepository;
import com.Martin.SaaS.repository.SuscripcionRepository;
import com.Martin.SaaS.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuscripcionServiceTest {

	@Mock
	private SuscripcionRepository suscripcionRepository;
	@Mock
	private FacturaRepository facturaRepository;
	@Mock
	private PlanRepository planRepository;
	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private SuscripcionService suscripcionService;

	private Usuario usuario;
	private Plan planBasic;
	private Plan planPremium;

	@BeforeEach
	void setUp() {
		usuario = Usuario.builder().id(1L).email("test@test.com").nombre("Test").build();
		usuario.setRole(com.Martin.SaaS.model.enums.Role.USER);
		usuario.setPerfil(Perfil.builder().pais("ES").usuario(usuario).build());

		planBasic = Plan.builder().id(1L).tipo(TipoPlan.BASIC).nombre("Basic").precioMensual(new BigDecimal("10.00"))
				.build();

		planPremium = Plan.builder().id(2L).tipo(TipoPlan.PREMIUM).nombre("Premium")
				.precioMensual(new BigDecimal("20.00")).build();
	}

	@Test
	void crearSuscripcion_DeberiaCrearSuscripcionYFactura() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(planRepository.findById(1L)).thenReturn(Optional.of(planBasic));
		when(suscripcionRepository.findSuscripcionActivaByUsuarioId(1L)).thenReturn(Optional.empty());
		when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(i -> i.getArguments()[0]);
		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		Suscripcion resultado = suscripcionService.crearSuscripcion(1L, 1L);

		assertNotNull(resultado);
		assertEquals(EstadoSuscripcion.ACTIVA, resultado.getEstado());
		verify(suscripcionRepository).save(any(Suscripcion.class));
		verify(facturaRepository).save(any(Factura.class));
	}

	@Test
	void generarFacturaMensual_DeberiaCalcularImpuestoEspana() {
		Suscripcion suscripcion = Suscripcion.builder().usuario(usuario).plan(planBasic).build();

		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		Factura factura = suscripcionService.generarFacturaMensual(suscripcion);

		assertNotNull(factura);
		assertTrue(new BigDecimal("21.00").compareTo(factura.getPorcentajeImpuesto()) == 0);
		assertTrue(new BigDecimal("12.10").compareTo(factura.getTotal()) == 0);
	}

	@Test
	void generarFacturaMensual_DeberiaCalcularImpuestoUSA() {
		usuario.getPerfil().setPais("US");
		Suscripcion suscripcion = Suscripcion.builder().usuario(usuario).plan(planBasic).build();

		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		Factura factura = suscripcionService.generarFacturaMensual(suscripcion);

		assertTrue(new BigDecimal("8.00").compareTo(factura.getPorcentajeImpuesto()) == 0);
		assertTrue(new BigDecimal("10.80").compareTo(factura.getTotal()) == 0);
	}

	@Test
	void cambiarPlan_UpgradeDeberiaGenerarProrrateo() {
		Suscripcion suscripcion = Suscripcion.builder().id(1L).usuario(usuario).plan(planBasic)
				.fechaInicio(LocalDate.now()).fechaProximoCobro(LocalDate.now().plusDays(15)) // Quedan 15 días
				.build();

		when(suscripcionRepository.findById(1L)).thenReturn(Optional.of(suscripcion));
		when(planRepository.findById(2L)).thenReturn(Optional.of(planPremium));
		when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(i -> i.getArguments()[0]);
		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		// Mock logic for upgrade check (Need to ensure logic in Plan works with mocks
		// or real objects)
		// Since we use real objects for Plan, logic should work if implemented in
		// Entity.
		// Let's verify Plan.esUpgradeDe logic:
		// Basic vs Premium. Ordinal comparison or specific logic?
		// Assuming Logic is correct in Plan entity.

		suscripcionService.cambiarPlan(1L, 2L);

		verify(facturaRepository)
				.save(argThat(factura -> factura.getTipo() == com.Martin.SaaS.model.enums.TipoFactura.PRORRATEO));
		assertEquals(planPremium, suscripcion.getPlan());
	}

	@Test
	void cancelarSuscripcion_DeberiaCambiarEstadoACancelada() {
		Suscripcion suscripcion = Suscripcion.builder().id(1L).estado(EstadoSuscripcion.ACTIVA).build();
		when(suscripcionRepository.findById(1L)).thenReturn(Optional.of(suscripcion));
		when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(i -> i.getArguments()[0]);

		Suscripcion resultado = suscripcionService.cancelarSuscripcion(1L, "Motivo de prueba");

		assertEquals(EstadoSuscripcion.CANCELADA, resultado.getEstado());
		verify(suscripcionRepository).save(suscripcion);
	}

	@Test
	void pagarFactura_DeberiaMarcarComoPagada() {
		Factura factura = Factura.builder().id(1L).estado(EstadoFactura.PENDIENTE).build();
		when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		Factura resultado = suscripcionService.pagarFactura(1L);

		assertEquals(EstadoFactura.PAGADA, resultado.getEstado());
		assertNotNull(resultado.getFechaPago());
		verify(facturaRepository).save(factura);
	}

	@Test
	void procesarCobrosAutomaticos_DeberiaRenovarSuscripcionesActivas() {
		Suscripcion s1 = Suscripcion.builder().id(1L).usuario(usuario).plan(planBasic).estado(EstadoSuscripcion.ACTIVA)
				.autoRenovacion(true).periodoFacturacionDias(30).fechaProximoCobro(LocalDate.now()).build();

		when(suscripcionRepository.findSuscripcionesParaCobrarHoy()).thenReturn(java.util.List.of(s1));
		when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArguments()[0]);

		suscripcionService.procesarCobrosAutomaticos();

		verify(facturaRepository, times(1)).save(any(Factura.class));
		verify(suscripcionRepository, times(1)).save(any(Suscripcion.class));
		assertTrue(s1.getFechaProximoCobro().isAfter(LocalDate.now()));
	}
}
