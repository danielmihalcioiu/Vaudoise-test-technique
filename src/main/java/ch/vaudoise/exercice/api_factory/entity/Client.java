/**
 * =============================================================
 *  File: Client.java
 *  Author: Daniel Mihalcioiu
 *  Description: Abstract base class representing a client entity.
 *               Can be a Person or a Company. Handles common
 *               attributes such as name, email, phone, and active state.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Client {

    /** Unique identifier for the client (auto-generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the client (person or company contact). */
    @NotBlank
    private String name;

    /** Validated email address. Must follow the standard email format. */
    @NotBlank
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Invalid email format"
    )
    @Column(nullable = false)
    private String email;

    /** Validated phone number allowing spaces, hyphens, and optional '+' prefix. */
    @Pattern(
        regexp = "^[+]?([0-9][\\s-]?){7,15}$",
        message = "Invalid phone number format"
    )
    private String phone;

    /**
     * List of contracts associated with the client.
     * The relationship is one-to-many (a client can have multiple contracts).
     * Ignored in JSON serialization to prevent circular references.
     */
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Contract> contracts;

    /**
     * Ensures phone number is stored without spaces or hyphens before saving or updating.
     */
    @PrePersist
    @PreUpdate
    public void normalizePhone() {
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
    }

    /** Indicates whether the client is active (used for soft delete). */
    @Column(nullable = false)
    private boolean active = true;
}
