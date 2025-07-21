package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.services.BeerOrderShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerOrderShipmentsController.class)
public class BeerOrderShipmentsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerOrderShipmentService beerOrderShipmentService;

    BeerOrderShipmentDto shipmentDto;

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
    }

    @Test
    void testGetShipmentsByBeerOrderId() throws Exception {
        // given
        List<BeerOrderShipmentDto> shipments = Arrays.asList(shipmentDto);
        when(beerOrderShipmentService.getShipmentsByBeerOrderId(1)).thenReturn(shipments);

        // when/then
        mockMvc.perform(get("/api/v1/orders/1/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].carrier", is("UPS")))
                .andExpect(jsonPath("$[0].trackingNumber", is("1234567890")))
                .andExpect(jsonPath("$[0].beerOrderId", is(1)));
    }

    @Test
    void testGetShipmentsByBeerOrderIdEmpty() throws Exception {
        // given
        List<BeerOrderShipmentDto> shipments = Arrays.asList();
        when(beerOrderShipmentService.getShipmentsByBeerOrderId(1)).thenReturn(shipments);

        // when/then
        mockMvc.perform(get("/api/v1/orders/1/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}