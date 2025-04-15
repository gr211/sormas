package lu.formas.services.model;

import lombok.Getter;
import lombok.val;
import lu.formas.repository.model.Vaccine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Getter
public class VaccinesByMaturity {
    private final Map<VaccinesByMaturityKey, List<VaccinesByMaturityValue>> entries;

    public VaccinesByMaturity(List<Vaccine> vaccines) {
        val collect = vaccines.stream()
                .map(VaccinesByMaturityValue::of)
                .collect(groupingBy(vaccine ->
                        new VaccinesByMaturityKey(vaccine.getVaccine().getMaturityMonth())
                ));

        collect.forEach((key, value) ->
                value.add(VaccinesByMaturityValue.placeholder(key))
        );

        entries = collect;
    }

    public List<VaccinesByMaturityValue> flatten() {
        List<VaccinesByMaturityValue> collect = entries.values().stream()
                .flatMap(List::stream)
                .sorted(VaccinesByMaturityValue.comparator())
                .collect(Collectors.toList());

        return collect;
    }
}
