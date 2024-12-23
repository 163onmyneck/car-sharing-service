package car.sharing.app.carsharingservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Update user role")
    @WithMockUser(username = "user", roles = {"MANAGER"})
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateRole() throws Exception {
        mockMvc.perform(put("/users/{id}/role", 2L)
                .param("role", "manager"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("get user's profile")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "test1@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void getMyProfile() throws Exception {
        UserDto expected = new UserDto()
                .setId(1L)
                .setEmail("test1@gmail.com")
                .setFirstName("Name1")
                .setLastName("Lastname1");

        MvcResult result = mockMvc.perform(put("/users/me")
                        .content(objectMapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("update user's profile")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails(value = "test1@gmail.com",
            userDetailsServiceBeanName = "customUserDetailsService")
    void updateProfile() throws Exception {
        String newEmail = "new@email.com";
        String newFirstName = "Oleh";

        UserDto dto = new UserDto()
                .setEmail(newEmail)
                .setFirstName(newFirstName);

        MvcResult result = mockMvc.perform(put("/users/me")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);

        assertEquals(actual.getEmail(), newEmail);
        assertEquals(actual.getFirstName(), newFirstName);
    }
}
