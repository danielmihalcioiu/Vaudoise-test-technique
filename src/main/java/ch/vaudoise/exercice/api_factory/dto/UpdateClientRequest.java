package ch.vaudoise.exercice.api_factory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateClientRequest {
    @NotBlank
    private String name;

    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Invalid email format"
    )
    private String email;

    @Pattern(
        regexp = "^[+]?([0-9][\\s-]?){7,15}$",
        message = "Invalid phone number format"
    )
    private String phone;
}
