package lu.formas.views.dashboard;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.security.LogoutView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route("dashboard")
@RolesAllowed({"USER"})
public class DashboardView extends AppLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;

    @Autowired
    public DashboardView(PatientService personService, AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = personService;

        if (Objects.nonNull(securityService.getAuthenticatedUser())) {
            createHeader();
            setContent(new ButtonsView());
        }
    }

    private void createHeader() {
        val logo = new Image("images/sormas.png", "Sormas");
        logo.setHeight("40px");
        logo.getStyle()
                .set("margin-left", "10px");

        val companyName = new H1("Sormas");
        companyName.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        val menu = createMenu();
        menu.setWidth("100%");
        menu.getStyle().set("justify-content", "flex-end");

        val logoLayout = new HorizontalLayout(logo, companyName);
        logoLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);

        val header = new HorizontalLayout(logoLayout, menu);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.add(menu);

        addToNavbar(header);
    }

    private Tabs createMenu() {
        val tabs = new Tabs();
        tabs.add(
                createTab("Profile"),
                createTab("Vaccination card"),
                createTab("Logout", LogoutView.class)
        );
        return tabs;
    }

    private Tab createTab(String text) {
        val tab = new Tab();
        tab.add(new Button(text));
        return tab;
    }

    private Tab createTab(String text, Class<? extends Component> navigationTarget) {
        val tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        return tab;
    }
}