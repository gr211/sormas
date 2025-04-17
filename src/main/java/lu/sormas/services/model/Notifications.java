package lu.sormas.services.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lu.sormas.repository.model.Vaccine;

import java.util.List;

@AllArgsConstructor
@ToString
public class Notifications {

    List<Vaccine> nextVaccines;
    List<Vaccine> overdueVaccines;

    public List<Vaccine> getOverdueVaccines() {
        overdueVaccines.sort((v1, v2) -> v1.getMaturityMonth() < v2.getMaturityMonth() ? -1 : 1);
        return overdueVaccines;
    }

    public List<Vaccine> getNextVaccines() {
        nextVaccines.sort((v1, v2) -> v1.getMaturityMonth() < v2.getMaturityMonth() ? -1 : 1);
        return nextVaccines;
    }
}
