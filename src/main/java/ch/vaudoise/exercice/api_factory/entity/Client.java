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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Invalid email format"
    )
    @Column(nullable = false)
    private String email;


    @Pattern(
        regexp = "^[+]?([0-9][\\s-]?){7,15}$",
        message = "Invalid phone number format"
    )
    private String phone;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Contract> contracts;

    @PrePersist
    @PreUpdate
    public void normalizePhone() {
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
    }

    @Column(nullable = false)
    private boolean active = true;
}
