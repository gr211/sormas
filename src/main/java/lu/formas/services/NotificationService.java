package lu.formas.services;

import lu.formas.repository.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class NotificationService {
    private final PatientService patientService;

    public NotificationService(PatientService patientService) {
        this.patientService = patientService;
    }

    public long ageInMonths(Patient patient) {
        return ChronoUnit.MONTHS.between(patient.getDob(),
                LocalDate.now().minusDays(1));
    }
}
