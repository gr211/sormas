package lu.formas.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.dashboard.DashboardView;
import lu.formas.views.profile.ProfileView;
import lu.formas.views.security.LogoutView;

@CssImport("./styles/shared-styles.css")
@Layout
@AnonymousAllowed
public class MainView extends AppLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;

    public MainView(PatientService personService, AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = personService;

        createHeader();
    }

    private void createHeader() {
        val sormasLogo = new Image("images/sormas.png", "Sormas");
        sormasLogo.setHeight("40px");
        sormasLogo.getStyle().set("margin-left", "10px");

        val sormasTitle = new H1("Sormas");
        sormasTitle.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        val menu = createMenu();
        menu.setWidth("100%");
        menu.getStyle().set("justify-content", "flex-end");

        val logoLayout = new HorizontalLayout(sormasLogo, sormasTitle);
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
                createTab("Vaccination card", DashboardView.class),
                createTab("Profile", ProfileView.class),
                createTab("Logout", LogoutView.class)
        );

        tabs.getTabAt(0).setSelected(true);

        return tabs;
    }

    private Tab createTab(String text, Class<? extends Component> navigationTarget) {
        val tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        return tab;
    }
}