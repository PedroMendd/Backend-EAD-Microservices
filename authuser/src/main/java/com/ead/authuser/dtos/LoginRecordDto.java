package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRecordDto(@NotBlank String username,
                             @NotBlank String password) {
}
