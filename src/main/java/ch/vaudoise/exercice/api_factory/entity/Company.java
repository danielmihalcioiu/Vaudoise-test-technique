package ch.vaudoise.exercice.api_factory.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Company extends Client {

    @NotBlank
    @Pattern(
        regexp = "^[a-zA-Z]{3}-\\d{3}$",
        message = "Company identifier must follow the pattern 'aaa-123'"
    )
    private String companyIdentifier;
}
