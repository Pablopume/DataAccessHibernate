package model.errors;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public sealed class OrderError permits OrderErrorEmptyList {
    private final String message;

    private final LocalDateTime fecha;

    public OrderError(String message) {
        this.message = message;
        this.fecha = LocalDateTime.now();
    }
}

