package car.sharing.app.carsharingservice.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import car.sharing.app.carsharingservice.dto.car.CarRequestDto;
import car.sharing.app.carsharingservice.dto.car.CarResponseDto;
import car.sharing.app.carsharingservice.model.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarControllerTest {
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
    @WithMockUser(username = "username", roles = {"MANAGER"})
    @DisplayName("Create new car, expecting it's dto back")
    @Sql(scripts = "classpath:database/clear-database.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCar() throws Exception {
        CarRequestDto carRequestDto = new CarRequestDto()
                .setCarType(Car.CarType.SEDAN)
                .setModel("G90")
                .setBrand("BMW")
                .setInventory(10)
                .setFeeUsd(BigDecimal.TEN);

        CarResponseDto expected = new CarResponseDto()
                .setFeeUsd(carRequestDto.getFeeUsd())
                .setCarType(carRequestDto.getCarType())
                .setModel(carRequestDto.getModel())
                .setBrand(carRequestDto.getBrand())
                .setInventory(carRequestDto.getInventory());

        String jsonRequest = objectMapper.writeValueAsString(carRequestDto);

        MvcResult result = mockMvc.perform(post("/cars")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("get all available cars")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/car/01-create-and-insert-cars.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "username", roles = {"CUSTOMER"})
    void getAllCars() throws Exception {
        int expectedSize = 2;

        MvcResult result = mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andReturn();

        CarResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarResponseDto[].class);

        Assertions.assertEquals(expectedSize, actual.length);
    }

    @Test
    @DisplayName("get all available cars")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/car/01-create-and-insert-cars.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "username", roles = {"CUSTOMER"})
    void getCarById() throws Exception {
        CarResponseDto expected = new CarResponseDto()
                .setModel("f10")
                .setBrand("bmw")
                .setCarType(Car.CarType.SEDAN)
                .setInventory(100)
                .setFeeUsd(BigDecimal.valueOf(500));

        MvcResult result = mockMvc.perform(get("/cars/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("update by id")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/car/01-create-and-insert-cars.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "username", roles = {"MANAGER"})
    void updateById() throws Exception {
        String newModel = "G90";
        CarRequestDto carRequestDto = new CarRequestDto()
                .setModel(newModel);

        String jsonRequest = objectMapper.writeValueAsString(carRequestDto);

        MvcResult result = mockMvc.perform(put("/cars/{id}", 1)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CarResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarResponseDto.class);

        Assertions.assertEquals(newModel, actual.getModel());
    }

    @Test
    @DisplayName("delete car by id")
    @Sql(scripts = {
            "classpath:database/clear-database.sql",
            "classpath:database/car/01-create-and-insert-cars.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "username", roles = {"MANAGER"})
    void deleteById() throws Exception {
        mockMvc.perform(delete("/cars/{id}", 1))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
