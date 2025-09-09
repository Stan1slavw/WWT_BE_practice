package com.wwt_be.authapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@Email String email, @NotBlank String password) {

}