package com.kotkina.quotesServiceApi.entities;

import com.kotkina.quotesServiceApi.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AssessmentType {
    LIKE((byte) 1), DISLIKE((byte) -1);

    private byte score;

    public static AssessmentType getType(byte value) {
        return Arrays.stream(AssessmentType.values())
                .filter(type -> type.getScore() == value)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No assessment found"));
    }
}
