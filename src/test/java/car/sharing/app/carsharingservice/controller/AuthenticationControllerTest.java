package car.sharing.app.carsharingservice.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import car.sharing.app.carsharingservice.dto.user.UserDto;
import car.sharing.app.carsharingservice.dto.user.UserRegistrationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthenticationControllerTest {
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
    @DisplayName("Register with valid dto")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/user/01-insert-roles.sql",
            "classpath:database/user/02-insert-2-users.sql",
            "classpath:database/user/03-fill-users-roles.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register() throws Exception {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto()
                .setEmail("email@email.com")
                .setPassword("passwor321d")
                .setFirstName("Andriy")
                .setLastName("XBOCT")
                .setRepeatPassword("passwor321d");

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserDto.class);

        Assertions.assertEquals(dto.getEmail(), actual.getEmail());
    }
}
