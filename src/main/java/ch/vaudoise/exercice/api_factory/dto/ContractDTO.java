/**
 * =============================================================
 *  File: ContractDTO.java
 *  Author: Daniel Mihalcioiu
 *  Description: Data Transfer Object used for creating or updating
 *               contract information via the API.
 *               Includes basic validation for required and numeric fields.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ContractDTO {

    /**
     * ID of the client associated with the contract.
     * Must reference an existing client.
     */
    private Long clientId;

    /**
     * Monetary value of the contract.
     * Must be a positive number.
     */
    @NotNull
    @Positive
    private Double costAmount;

    /**
     * Start date of the contract.
     * Defaults to the current date if not provided.
     */
    private LocalDate startDate;

    /**
     * End date of the contract (optional).
     * Null value indicates that the contract is still active.
     */
    private LocalDate endDate;
}
