/**
 * =============================================================
 *  File: UpdateClientRequest.java
 *  Author: Daniel Mihalcioiu
 *  Description: Data Transfer Object for updating client information.
 *               Excludes immutable fields such as birthDate and companyIdentifier.
 *               Includes validation for proper formatting of email and phone.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientRequest {

    /**
     * Updated name of the client.
     * Cannot be null or blank.
     */
    @NotBlank
    private String name;

    /**
     * Updated email address of the client.
     * Must follow a valid email format.
     */
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Invalid email format"
    )
    private String email;

    /**
     * Updated phone number of the client.
     * Accepts numbers with optional '+' prefix and no special characters.
     */
    @Pattern(
        regexp = "^[+]?([0-9][\\s-]?){7,15}$",
        message = "Invalid phone number format"
    )
    private String phone;
}
