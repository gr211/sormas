package lu.formas.services;

import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import lombok.val;
import lombok.var;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final PatientService patientService;
    private final VaccineService vaccineService;

    public NotificationService(PatientService patientService, VaccineService vaccineService) {
        this.patientService = patientService;
        this.vaccineService = vaccineService;
    }

    public long ageInMonths(Patient patient) {
        return ChronoUnit.MONTHS.between(patient.getDob(),
                LocalDate.now().minusDays(1));
    }

    public List<Vaccine> nextVaccines(Patient patient) {
        val months = ageInMonths(patient);

        var alreadyVaccinated = patient.getPatientVaccines();

        val vaccines = Stream.ofAll(vaccineService.vaccines()).filter(vaccine -> Objects.isNull(alreadyVaccinated) ||
                alreadyVaccinated.stream().noneMatch(pv -> pv.getVaccine().equals(vaccine)));

        val futureVaccines = vaccines.filter(vaccine -> vaccine.getMaturityMonth() >= months);
        val nextEligibleMaturityMonth = futureVaccines.minBy(Vaccine::getMaturityMonth);

        if (nextEligibleMaturityMonth.isEmpty()) { // no more vaccines coming up
            return Collections.emptyList();
        }

        val eligibleMaturityMonthVaccines = futureVaccines.filter(e -> e.getMaturityMonth().intValue() == nextEligibleMaturityMonth.get().getMaturityMonth().intValue());

        // this may seem a little complicated. Yet it nicely does away with issues around static initialization since alreadyVaccinated may be null.
        val streamedAlreadyVaccinated = Stream.continually(() -> {
            if (Objects.isNull(alreadyVaccinated)) {
                return Collections.<PatientVaccine>emptySet();
            }

            return alreadyVaccinated;
        }).map(patientVaccines -> Stream.ofAll(patientVaccines).map(PatientVaccine::getVaccine).collect(Collectors.toList()));

        val remainingVaccines = streamedAlreadyVaccinated.zip(eligibleMaturityMonthVaccines)
                .filter((tuples) -> !tuples._1.contains(tuples._2));


        return remainingVaccines.map(Tuple2::_2).collect(Collectors.toList());
    }

    public List<Vaccine> overdueVaccines(Patient patient) {
        val months = ageInMonths(patient);

        val vaccines = vaccineService.vaccines();

        var alreadyVaccinated = patient.getPatientVaccines();

        val pastVaccines = Stream.ofAll(vaccines).filter(vaccine -> vaccine.getMaturityMonth() < months);

        val overdueVaccines = pastVaccines.filter(vaccine -> Objects.isNull(alreadyVaccinated) ||
                alreadyVaccinated.stream().noneMatch(pv -> pv.getVaccine().equals(vaccine)));

        return overdueVaccines.collect(Collectors.toList());
    }
}
