package ch.vaudoise.exercice.api_factory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ContractDTO {
    private Long clientId;

    @NotNull
    @Positive
    private Double costAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
