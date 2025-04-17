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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
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
public class MainView extends AppLayout implements BeforeEnterObserver {

    transient AuthenticationContext authContext;

    private final SecurityService securityService;
    private final PatientService personService;

    Tabs menu;

    public MainView(PatientService personService, AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = personService;

        createHeader();
    }

    private void createHeader() {
        val sormasLogo = new Image("images/sormas.png", "Sormas");
        sormasLogo.setClassName("header-logo");

        val sormasTitle = new H1("Sormas");
        sormasTitle.setClassName("header-title");

        val logoLayout = new HorizontalLayout(sormasLogo, sormasTitle);
        logoLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(true);

        val header = new HorizontalLayout(logoLayout);
        header.setClassName("header");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        menu = new Tabs();
        menu.setClassName("header-menu");
        this.createMenu();
        header.add(menu);

        addToNavbar(header);
    }

    private void createMenu() {
        menu.add(
                createTab("Vaccination card", DashboardView.class),
                createTab("Profile", ProfileView.class),
                createTab("Logout", LogoutView.class)
        );

        menu.getTabAt(0).setSelected(true);
    }

    private Tab createTab(String text, Class<? extends Component> navigationTarget) {
        val tab = new Tab();

        tab.add(new RouterLink(text, navigationTarget));
        return tab;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }
}