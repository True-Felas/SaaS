package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para cambiar de plan.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPlanRequest {
	private Long suscripcionId;
	private Long nuevoPlanId;
}
