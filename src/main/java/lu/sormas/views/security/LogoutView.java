package lu.sormas.views.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lu.sormas.security.SecurityService;

@Route(value = "logout", autoLayout = false)
@AnonymousAllowed
public class LogoutView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;

    public LogoutView(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        logout();
        UI.getCurrent().getPage().setLocation("login");
    }

    public void logout() {
        securityService.logout();
    }
}