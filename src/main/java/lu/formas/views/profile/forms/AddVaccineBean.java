package lu.formas.views.profile.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lu.formas.services.model.VaccinesByMaturityValue;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddVaccineBean {

    @NotEmpty(message = "Password is required")
    @Size(message = "Maximum size is 128", max = 128)
    private LocalDate datePicker;

    @NotEmpty(message = "Password is required")
    @Size(message = "Maximum size is 128", max = 128)
    private VaccinesByMaturityValue select;

    @Size(message = "Maximum size is 512", max = 512)
    private String comments;
}
