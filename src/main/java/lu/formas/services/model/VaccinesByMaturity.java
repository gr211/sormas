package lu.formas.services.model;

import lombok.Getter;
import lu.formas.repository.model.Vaccine;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Getter
public class VaccinesByMaturity {
    private final Map<VaccinesByMaturityKey, List<Vaccine>> entries;

    public VaccinesByMaturity(List<Vaccine> vaccines) {

        entries = vaccines.stream().collect(groupingBy(vaccine ->
                new VaccinesByMaturityKey(vaccine.getMaturityMonth())
        ));
    }
}
