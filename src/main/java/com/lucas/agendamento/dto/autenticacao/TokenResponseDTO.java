package com.lucas.agendamento.dto.autenticacao;

public record TokenResponseDTO(String token, long expiresIn) {}
