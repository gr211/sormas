package lu.formas.views.dashboard;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route("dashboard")
@RolesAllowed({"USER"})
@CssImport("./styles/shared-styles.css")
public class DashboardView extends MainView {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;

    @Autowired
    public DashboardView(PatientService patientService, AuthenticationContext authContext, SecurityService securityService) {
        super(patientService, authContext, securityService);

        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = patientService;

        setContent(new VaccinationHistoryView());
    }
}