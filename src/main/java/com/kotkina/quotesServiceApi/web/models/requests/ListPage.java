package com.kotkina.quotesServiceApi.web.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListPage {

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "5";

    @NotBlank
    @PositiveOrZero
    @Schema(defaultValue = PAGE_NUMBER, description = "Page number")
    private int page;

    @NotBlank
    @Positive
    @Schema(defaultValue = PAGE_SIZE, description = "Number of items on the page")
    private int size;

    public ListPage() {
        this.page = Integer.valueOf(PAGE_NUMBER);
        this.size = Integer.valueOf(PAGE_SIZE);
    }
}
