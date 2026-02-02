package com.Martin.SaaS.controller;

import com.Martin.SaaS.dto.PlanDTO;
import com.Martin.SaaS.mapper.EntityMapper;
import com.Martin.SaaS.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de planes.
 */
@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlanController {

    private final PlanService planService;
    private final EntityMapper mapper;

    /**
     * Obtiene todos los planes activos.
     */
    @GetMapping
    public ResponseEntity<List<PlanDTO>> obtenerPlanes() {
        List<PlanDTO> planes = planService.obtenerPlanesActivos().stream()
                .map(mapper::toPlanDTO)
                .toList();
        return ResponseEntity.ok(planes);
    }

    /**
     * Obtiene un plan por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanDTO> obtenerPlan(@PathVariable Long id) {
        return planService.obtenerPorId(id)
                .map(mapper::toPlanDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
