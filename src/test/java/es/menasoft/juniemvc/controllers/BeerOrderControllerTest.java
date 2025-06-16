package es.menasoft.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;
import es.menasoft.juniemvc.models.OrderLineDto;
import es.menasoft.juniemvc.services.BeerOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BeerOrderControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        BeerOrderService beerOrderService() {
            return Mockito.mock(BeerOrderService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerOrderService beerOrderService;

    private BeerOrderDto testBeerOrder;
    private List<BeerOrderDto> testBeerOrderList;
    private OrderLineDto testOrderLine;
    private CreateBeerOrderCommand testCreateCommand;

    @BeforeEach
    void setUp() {
        testOrderLine = new OrderLineDto(
                1,
                5,
                1,
                "Test Beer"
        );

        testBeerOrder = new BeerOrderDto(
                1,
                null,
                "NEW",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                List.of(testOrderLine)
        );

        BeerOrderDto testBeerOrder2 = new BeerOrderDto(
                2,
                null,
                "PROCESSING",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                Collections.emptyList()
        );

        testBeerOrderList = Arrays.asList(testBeerOrder, testBeerOrder2);

        testCreateCommand = new CreateBeerOrderCommand(
                1,
                List.of(new OrderLineDto(null, 5, 1, null))
        );
    }

    @Test
    void testCreateBeerOrder() throws Exception {
        // Given
        given(beerOrderService.createBeerOrder(any(CreateBeerOrderCommand.class))).willReturn(testBeerOrder);

        // When/Then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderStatus", is("NEW")))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.orderLines", hasSize(1)));
    }

    @Test
    void testCreateBeerOrderValidationFail() throws Exception {
        // Given
        CreateBeerOrderCommand invalidCommand = new CreateBeerOrderCommand(
                null, // Invalid: null customerId
                Collections.emptyList() // Invalid: empty orderLines
        );

        // When/Then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
    }

    @Test
    void testGetBeerOrderById() throws Exception {
        // Given
        given(beerOrderService.getBeerOrderById(1)).willReturn(Optional.of(testBeerOrder));

        // When/Then
        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderStatus", is("NEW")))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.orderLines", hasSize(1)));
    }

    @Test
    void testGetBeerOrderByIdNotFound() throws Exception {
        // Given
        given(beerOrderService.getBeerOrderById(999)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBeerOrders() throws Exception {
        // Given
        given(beerOrderService.getAllBeerOrders()).willReturn(testBeerOrderList);

        // When/Then
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].orderStatus", is("NEW")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].orderStatus", is("PROCESSING")));
    }

    @Test
    void testGetBeerOrdersByCustomerId() throws Exception {
        // Given
        given(beerOrderService.getBeerOrdersByCustomerId(1)).willReturn(testBeerOrderList);

        // When/Then
        mockMvc.perform(get("/api/v1/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].customerId", is(1)));
    }

    @Test
    void testUpdateBeerOrder() throws Exception {
        // Given
        BeerOrderDto beerOrderToUpdate = new BeerOrderDto(
                null,
                null,
                "COMPLETED",
                null,
                null,
                1,
                List.of(testOrderLine)
        );

        BeerOrderDto updatedBeerOrder = new BeerOrderDto(
                1,
                null,
                "COMPLETED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                List.of(testOrderLine)
        );

        given(beerOrderService.updateBeerOrder(eq(1), any(BeerOrderDto.class))).willReturn(Optional.of(updatedBeerOrder));

        // When/Then
        mockMvc.perform(put("/api/v1/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderStatus", is("COMPLETED")));
    }

    @Test
    void testUpdateBeerOrderNotFound() throws Exception {
        // Given
        BeerOrderDto beerOrderToUpdate = new BeerOrderDto(
                null,
                null,
                "COMPLETED",
                null,
                null,
                1,
                List.of(testOrderLine)
        );

        given(beerOrderService.updateBeerOrder(eq(999), any(BeerOrderDto.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/orders/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBeerOrderStatus() throws Exception {
        // Given
        BeerOrderDto updatedBeerOrder = new BeerOrderDto(
                1,
                null,
                "COMPLETED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                List.of(testOrderLine)
        );

        given(beerOrderService.updateBeerOrderStatus(eq(1), eq("COMPLETED"))).willReturn(Optional.of(updatedBeerOrder));

        // When/Then
        mockMvc.perform(patch("/api/v1/orders/1/status")
                .contentType(MediaType.TEXT_PLAIN)
                .content("COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderStatus", is("COMPLETED")));
    }

    @Test
    void testUpdateBeerOrderStatusNotFound() throws Exception {
        // Given
        given(beerOrderService.updateBeerOrderStatus(eq(999), eq("COMPLETED"))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(patch("/api/v1/orders/999/status")
                .contentType(MediaType.TEXT_PLAIN)
                .content("COMPLETED"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBeerOrder() throws Exception {
        // Given
        given(beerOrderService.deleteBeerOrder(1)).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(beerOrderService).deleteBeerOrder(1);
    }

    @Test
    void testDeleteBeerOrderNotFound() throws Exception {
        // Given
        given(beerOrderService.deleteBeerOrder(999)).willReturn(false);

        // When/Then
        mockMvc.perform(delete("/api/v1/orders/999"))
                .andExpect(status().isNotFound());
    }
}