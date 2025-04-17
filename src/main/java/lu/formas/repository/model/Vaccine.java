package lu.formas.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "vaccines")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"patientVaccines"})
@EqualsAndHashCode(of = {"id"})
public class Vaccine {
    @Id
    private Long id;

    @Size(max = 256)
    private String name;

    @Min(value = 0)
    private Integer maturityMonth;

    @Size(max = 256)
    private String description;

    @Size(max = 256)
    private String goals;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PatientVaccine> patientVaccines;
}
