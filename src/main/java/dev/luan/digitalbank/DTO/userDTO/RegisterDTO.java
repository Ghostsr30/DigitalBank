package dev.luan.digitalbank.DTO.userDTO;

import dev.luan.digitalbank.domain.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
