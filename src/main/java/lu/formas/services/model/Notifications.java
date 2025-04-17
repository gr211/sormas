package lu.formas.services.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.formas.repository.model.Vaccine;

import java.util.List;

@AllArgsConstructor
@Getter
public class Notifications {

    List<Vaccine> nextVaccines;
    List<Vaccine> overdueVaccines;

}
