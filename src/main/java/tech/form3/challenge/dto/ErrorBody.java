package tech.form3.challenge.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Bean carrying the REST error information.
 */
@Data
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorBody {

    final Integer status;
    final String code;
    final String message;
    final String description;
    final String service;
}