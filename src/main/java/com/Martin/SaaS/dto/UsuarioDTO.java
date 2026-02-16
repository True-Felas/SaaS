package com.Martin.SaaS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para transferir información de usuarios.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	private Long id;
	private String email;
	private String nombre;
	private String apellido;
	private Boolean activo;
	private Boolean emailVerificado;
	private LocalDateTime fechaRegistro;
	private SuscripcionDTO suscripcionActiva;
	private String role;
}
