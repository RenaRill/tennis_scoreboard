package com.tennisscoreboard.model;

import com.tennisscoreboard.exception.InvalidValueException;
import lombok.Getter;

@Getter
public enum Points {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String score;

    Points(String score) {
        this.score = score;
    }

    Points next() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            default -> throw new InvalidValueException("Другие случаи покрываются самим геймом.");
        };
    }
}