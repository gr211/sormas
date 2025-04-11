package lu.formas.security;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testLoginAccessToUserDashboardWithNoRole() {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void testAccessToUserDashboardWithNoRole() {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "normalUser", roles = {"USER"})
    public void testAccessToAdminDashboardWithUserRole() {
        mockMvc.perform(MockMvcRequestBuilders.get("/view"))
                .andExpect(status().isNotFound());
    }
}