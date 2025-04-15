package lu.formas.views.dashboard;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;
import lu.formas.views.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed({"USER"})
@Tag("div")
@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Vaccination card")
public class DashboardView extends VerticalLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;
    private final VaccineService vaccineService;

    @Autowired
    public DashboardView(PatientService patientService, VaccineService vaccineService, AuthenticationContext authContext, SecurityService securityService) {

        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = patientService;
        this.vaccineService = vaccineService;

        add(new VaccinationHistoryView(patientService, vaccineService, securityService));
    }
}