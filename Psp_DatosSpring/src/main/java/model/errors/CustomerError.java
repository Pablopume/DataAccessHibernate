package model.errors;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public sealed class CustomerError permits CustomerErrorEmptyList {
    private final int numError;
    private final String message;

    private final LocalDateTime date;

    public CustomerError(int numError, String message) {
        this.numError = numError;
        this.message = message;
        this.date = LocalDateTime.now();

    }
}
