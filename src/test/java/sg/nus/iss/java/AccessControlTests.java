package sg.nus.iss.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import sg.nus.iss.java.model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessControlTests {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession adminSession;
    private MockHttpSession employeeSession;
    private MockHttpSession managerSession;

    @BeforeEach
    public void setup() {
        adminSession = new MockHttpSession();
        User admin = new User();
        admin.setUsername("admin");
        admin.setType("Admin");
        adminSession.setAttribute("user", admin);
        adminSession.setAttribute("role", "Admin");

        employeeSession = new MockHttpSession();
        User employee = new User();
        employee.setUsername("employee");
        employee.setType("Employee");
        employeeSession.setAttribute("user", employee);
        employeeSession.setAttribute("role", "Employee");

        managerSession = new MockHttpSession();
        User manager = new User();
        manager.setUsername("manager");
        manager.setType("Manager");
        managerSession.setAttribute("user", manager);
        managerSession.setAttribute("role", "Manager");
    }

    @Test
    public void testEmployeeCannotAccessAdminPath() throws Exception {
        mockMvc.perform(get("/leaveQuota/view").session(employeeSession))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testAdminCanAccessAdminPath() throws Exception {
        mockMvc.perform(get("/leaveQuota/view").session(adminSession))
                .andExpect(status().isOk());
    }

   

    @Test
    public void testUnauthenticatedUserCannotAccessProtectedPath() throws Exception {
        mockMvc.perform(get("/leaveQuota/view"))
                .andExpect(status().is3xxRedirection());
    }
}
