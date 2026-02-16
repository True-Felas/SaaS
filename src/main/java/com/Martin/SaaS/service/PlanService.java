package com.Martin.SaaS.service;

import com.Martin.SaaS.model.Plan;
import com.Martin.SaaS.model.enums.TipoPlan;
import com.Martin.SaaS.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Servicio para gestión de planes.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

	private final PlanRepository planRepository;

	// Obtiene todos los planes activos.
	public List<Plan> obtenerPlanesActivos() {
		return planRepository.findByActivoTrueOrderByPrecioMensualAsc();
	}

	// Obtiene un plan por ID.
	public Optional<Plan> obtenerPorId(Long id) {
		return planRepository.findById(id);
	}

	// Obtiene un plan por tipo.
	public Optional<Plan> obtenerPorTipo(TipoPlan tipo) {
		return planRepository.findByTipo(tipo);
	}

	// Obtiene todos los planes.
	public List<Plan> obtenerTodos() {
		return planRepository.findAll();
	}
}
