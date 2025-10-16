/**
 * =============================================================
 *  File: Contract.java
 *  Author: Daniel Mihalcioiu
 *  Description: Represents an insurance contract linked to a client.
 *               Contains cost, start/end dates, and last update timestamp.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Contract {

    /** Unique identifier for the contract (auto-generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated client (many contracts can belong to one client).
     * Uses LAZY loading for better performance and prevents infinite recursion.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    /** Start date of the contract. Defaults to the current date if not provided. */
    @NotNull
    private LocalDate startDate = LocalDate.now();

    /** End date of the contract. Can be null for open-ended contracts. */
    private LocalDate endDate;

    /** Cost amount of the contract. Must be positive. */
    @NotNull
    @Positive
    private Double costAmount;

    /** Timestamp of the last update (automatically maintained). */
    @Column(nullable = false)
    private LocalDateTime updateDate = LocalDateTime.now();

    /**
     * Updates the 'updateDate' timestamp before saving modifications.
     */
    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    /**
     * Initializes default dates when a new contract is created.
     */
    @PrePersist
    public void onCreate() {
        if (startDate == null) startDate = LocalDate.now();
        if (updateDate == null) updateDate = LocalDateTime.now();
    }
}
