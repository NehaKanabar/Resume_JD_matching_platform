package com.resume.resumematching.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T data;
    private Object errors;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(true)
                .message(message)
                .data(data)
                .errors(null)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message, Object errors) {
        return ApiResponse.<T>builder()
                .status(false)
                .message(message)
                .data(null)
                .errors(errors)
                .build();
    }
}
