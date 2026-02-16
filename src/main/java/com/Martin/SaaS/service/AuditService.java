package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Plan;
import com.Martin.SaaS.model.Suscripcion;
import com.Martin.SaaS.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Servicio para consultar el historial de auditoría (Envers).
@Service
@RequiredArgsConstructor
public class AuditService {

	@PersistenceContext
	private EntityManager entityManager;

	// Obtiene el historial completo de cambios de todas las suscripciones. Solo
	// para ADMIN.
	@Transactional(readOnly = true)
	public List<Map<String, Object>> obtenerHistorialSuscripciones() {
		AuditReader reader = AuditReaderFactory.get(entityManager);

		// Query para obtener todas las revisiones de Suscripcion
		AuditQuery query = reader.createQuery().forRevisionsOfEntity(Suscripcion.class, false, true)
				.addOrder(AuditEntity.revisionProperty("timestamp").desc());

		List<Object[]> resultados = query.getResultList();
		List<Map<String, Object>> historial = new ArrayList<>();

		for (Object[] fila : resultados) {
			Suscripcion suscripcion = (Suscripcion) fila[0];
			Object revisionEntity = fila[1]; // CustomRevisionEntity si existiera, o DefaultRevisionEntity
			// Envers por defecto usa DefaultRevisionEntity que tiene id y timestamp (long)

			// Extraer metadatos de la revisión (timestamp es lo más común)
			// Nota: Para acceder al usuario que hizo el cambio, se necesitaría una
			// CustomRevisionEntity
			// configurada con un RevisionListener. Asumiremos timestamp por ahora.

			Map<String, Object> entrada = new HashMap<>();
			entrada.put("entidad", "Suscripcion");
			entrada.put("accion", obtenerTipoRevision(fila[2]));
			entrada.put("usuario", suscripcion.getUsuario().getEmail()); // Usuario dueño de la suscripción
			entrada.put("plan", suscripcion.getPlan().getNombre());
			entrada.put("estado", suscripcion.getEstado());
			entrada.put("fecha", obtenerFechaRevision(revisionEntity));

			String detalles = String.format("Plan: %s, Estado: %s", suscripcion.getPlan().getNombre(),
					suscripcion.getEstado());
			entrada.put("detalles", detalles);

			historial.add(entrada);
		}

		return historial;
	}

	// Obtiene el historial de cambios de planes (precios, características).
	@Transactional(readOnly = true)
	public List<Map<String, Object>> obtenerHistorialPlanes() {
		AuditReader reader = AuditReaderFactory.get(entityManager);

		AuditQuery query = reader.createQuery().forRevisionsOfEntity(Plan.class, false, true)
				.addOrder(AuditEntity.revisionProperty("timestamp").desc());

		List<Object[]> resultados = query.getResultList();
		List<Map<String, Object>> historial = new ArrayList<>();

		for (Object[] fila : resultados) {
			Plan plan = (Plan) fila[0];
			Object revisionEntity = fila[1];

			Map<String, Object> entrada = new HashMap<>();
			entrada.put("entidad", "Plan");
			entrada.put("accion", obtenerTipoRevision(fila[2]));
			entrada.put("usuario", "SYSTEM"); // Cambios de planes suelen ser administrativos
			entrada.put("nombre", plan.getNombre());
			entrada.put("precio", plan.getPrecioMensual());
			entrada.put("fecha", obtenerFechaRevision(revisionEntity));

			String detalles = String.format("Precio: %s, Activo: %s", plan.getPrecioMensual(), plan.getActivo());
			entrada.put("detalles", detalles);

			historial.add(entrada);
		}

		return historial;
	}

	private String obtenerTipoRevision(Object revisionType) {
		// RevisionType es un enum de Envers: ADD, MOD, DEL
		return revisionType.toString();
	}

	private LocalDateTime obtenerFechaRevision(Object revisionEntity) {
		// Reflexión básica o casteo si conocemos la clase de revisión
		try {
			// DefaultRevisionEntity tiene un método getTimestamp() que devuelve long
			long timestamp = (long) revisionEntity.getClass().getMethod("getTimestamp").invoke(revisionEntity);
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
		} catch (Exception e) {
			return LocalDateTime.now();
		}
	}
}
