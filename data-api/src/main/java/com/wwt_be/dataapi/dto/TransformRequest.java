package com.wwt_be.dataapi.dto;

import jakarta.validation.constraints.NotBlank;

public record TransformRequest(@NotBlank String text) {

}