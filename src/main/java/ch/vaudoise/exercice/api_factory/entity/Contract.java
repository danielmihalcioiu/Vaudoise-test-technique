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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    @NotNull
    private LocalDate startDate = LocalDate.now();

    private LocalDate endDate;

    @NotNull
    @Positive
    private Double costAmount;

    @Column(nullable = false)
    private LocalDateTime updateDate = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        if (startDate == null) startDate = LocalDate.now();
        if (updateDate == null) updateDate = LocalDateTime.now();
    }
}
