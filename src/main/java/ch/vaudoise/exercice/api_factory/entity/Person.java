/**
 * =============================================================
 *  File: Person.java
 *  Author: Daniel Mihalcioiu
 *  Description: Represents an individual client with a mandatory
 *               birth date field (non-editable after creation).
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Person extends Client {

    /** Birth date of the client (ISO 8601 format). */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
