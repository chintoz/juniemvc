package es.menasoft.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;
import es.menasoft.juniemvc.services.BeerOrderShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerOrderShipmentController.class)
public class BeerOrderShipmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerOrderShipmentService beerOrderShipmentService;

    BeerOrderShipmentDto shipmentDto;
    CreateBeerOrderShipmentCommand createCommand;

    @BeforeEach
    void setUp() {
        shipmentDto = new BeerOrderShipmentDto(
                1,
                null,
                LocalDate.now(),
                "UPS",
                "1234567890",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1
        );

        createCommand = new CreateBeerOrderShipmentCommand(
                1,
                LocalDate.now(),
                "UPS",
                "1234567890"
        );
    }

    @Test
    void testCreateShipment() throws Exception {
        // given
        when(beerOrderShipmentService.createShipment(any())).thenReturn(shipmentDto);

        // when/then
        mockMvc.perform(post("/api/v1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("UPS")))
                .andExpect(jsonPath("$.trackingNumber", is("1234567890")))
                .andExpect(jsonPath("$.beerOrderId", is(1)));
    }

    @Test
    void testCreateShipmentValidationError() throws Exception {
        // given
        CreateBeerOrderShipmentCommand invalidCommand = new CreateBeerOrderShipmentCommand(
                null,
                null,
                "",
                ""
        );

        // when/then
        mockMvc.perform(post("/api/v1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetShipmentById() throws Exception {
        // given
        when(beerOrderShipmentService.getShipmentById(1)).thenReturn(Optional.of(shipmentDto));

        // when/then
        mockMvc.perform(get("/api/v1/shipments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("UPS")))
                .andExpect(jsonPath("$.trackingNumber", is("1234567890")))
                .andExpect(jsonPath("$.beerOrderId", is(1)));
    }

    @Test
    void testGetShipmentByIdNotFound() throws Exception {
        // given
        when(beerOrderShipmentService.getShipmentById(1)).thenReturn(Optional.empty());

        // when/then
        mockMvc.perform(get("/api/v1/shipments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllShipments() throws Exception {
        // given
        List<BeerOrderShipmentDto> shipments = Arrays.asList(shipmentDto);
        when(beerOrderShipmentService.getAllShipments()).thenReturn(shipments);

        // when/then
        mockMvc.perform(get("/api/v1/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].carrier", is("UPS")))
                .andExpect(jsonPath("$[0].trackingNumber", is("1234567890")))
                .andExpect(jsonPath("$[0].beerOrderId", is(1)));
    }

    @Test
    void testUpdateShipment() throws Exception {
        // given
        when(beerOrderShipmentService.updateShipment(eq(1), any())).thenReturn(Optional.of(shipmentDto));

        // when/then
        mockMvc.perform(put("/api/v1/shipments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("UPS")))
                .andExpect(jsonPath("$.trackingNumber", is("1234567890")))
                .andExpect(jsonPath("$.beerOrderId", is(1)));
    }

    @Test
    void testUpdateShipmentNotFound() throws Exception {
        // given
        when(beerOrderShipmentService.updateShipment(eq(1), any())).thenReturn(Optional.empty());

        // when/then
        mockMvc.perform(put("/api/v1/shipments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteShipment() throws Exception {
        // given
        when(beerOrderShipmentService.deleteShipment(1)).thenReturn(true);

        // when/then
        mockMvc.perform(delete("/api/v1/shipments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShipmentNotFound() throws Exception {
        // given
        when(beerOrderShipmentService.deleteShipment(1)).thenReturn(false);

        // when/then
        mockMvc.perform(delete("/api/v1/shipments/1"))
                .andExpect(status().isNotFound());
    }
}