package lu.formas.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.val;
import lombok.var;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/login";

    public UserDetails getAuthenticatedUser() {
        val context = SecurityContextHolder.getContext();
        val authentication = context.getAuthentication();

        if (Objects.nonNull(authentication)) {
            val principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }

            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;

                val username = jwt.getSubject();

                var roles =  jwt.getClaimAsStringList("roles");
                if(Objects.isNull(roles)) {
                    roles = Collections.emptyList();
                }

                return new User(username, "", roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            }
        }

        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}