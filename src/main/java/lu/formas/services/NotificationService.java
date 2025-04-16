package lu.formas.services;

import io.vavr.collection.Stream;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.Vaccine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
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

        val vaccines = vaccineService.vaccines();

        val futureVaccines = Stream.ofAll(vaccines).filter(v -> v.getMaturityMonth() >= months);
        val nextEligibleMaturityMonth = futureVaccines.minBy(Vaccine::getMaturityMonth);

        if (nextEligibleMaturityMonth.isEmpty()) { // no more vaccines coming up
            return Collections.emptyList();
        }

        val eligibleMaturityMonthVaccines = futureVaccines.filter(e -> e.getMaturityMonth().intValue() == nextEligibleMaturityMonth.get().getMaturityMonth().intValue());

        return eligibleMaturityMonthVaccines.collect(Collectors.toList());
    }
}
