package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para transferir información pública de usuarios (sin email).
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUsuarioDTO {
	private Long id;
	private String nombre;
	private String apellido;
	private String planNombre;
	private LocalDateTime fechaRegistro;
}
