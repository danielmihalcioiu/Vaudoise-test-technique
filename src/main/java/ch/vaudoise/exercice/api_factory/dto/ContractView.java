/**
 * =============================================================
 *  File: ContractView.java
 *  Author: Daniel Mihalcioiu
 *  Description: Read-only Data Transfer Object used for exposing
 *               contract information to API consumers.
 *               Excludes internal fields such as updateDate.
 * =============================================================
 */

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
    /**
     * Converts a Contract entity to a read-only ContractView.
     * Used to prevent exposing sensitive or internal data.
     *
     * @param c the Contract entity
     * @return a simplified view of the contract
     */
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
