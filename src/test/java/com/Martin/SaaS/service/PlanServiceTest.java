package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Plan;
import com.Martin.SaaS.model.enums.TipoPlan;
import com.Martin.SaaS.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

	@Mock
	private PlanRepository planRepository;

	@InjectMocks
	private PlanService planService;

	@Test
	void obtenerPlanesActivos_DeberiaRetornarSoloActivos() {
		Plan p1 = Plan.builder().id(1L).nombre("Basic").activo(true).build();
		when(planRepository.findByActivoTrueOrderByPrecioMensualAsc()).thenReturn(List.of(p1));

		List<Plan> resultado = planService.obtenerPlanesActivos();

		assertEquals(1, resultado.size());
		verify(planRepository).findByActivoTrueOrderByPrecioMensualAsc();
	}

	@Test
	void obtenerPorId_DeberiaRetornarPlan() {
		Plan p1 = Plan.builder().id(1L).build();
		when(planRepository.findById(1L)).thenReturn(Optional.of(p1));

		Optional<Plan> resultado = planService.obtenerPorId(1L);

		assertTrue(resultado.isPresent());
		assertEquals(1L, resultado.get().getId());
	}
}
