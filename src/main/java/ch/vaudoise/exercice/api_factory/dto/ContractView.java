package ch.vaudoise.exercice.api_factory.dto;

import ch.vaudoise.exercice.api_factory.entity.Contract;
import java.time.LocalDate;

public record ContractView(
        Long id,
        Double costAmount,
        LocalDate startDate,
        LocalDate endDate,
        Long clientId,
        String clientName
) {
    public static ContractView from(Contract c) {
        return new ContractView(
            c.getId(),
            c.getCostAmount(),
            c.getStartDate(),
            c.getEndDate(),
            c.getClient().getId(),
            c.getClient().getName()
        );
    }
}
