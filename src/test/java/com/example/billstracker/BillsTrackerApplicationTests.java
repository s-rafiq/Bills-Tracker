package com.example.billstracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc for testing API endpoints

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for converting objects to JSON

    private Bill sampleBill;

    @BeforeEach
    void setUp() {
        sampleBill = new Bill(1, "Sample Bill", 100.00, LocalDate.of(2023, 12, 31));
    }

    @Test
    void testGetAllBills() throws Exception {
        // Initially, the list of bills should be empty
        mockMvc.perform(get("/bills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testAddBill() throws Exception {
        // Add a new bill
        mockMvc.perform(post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleBill.getId())))
                .andExpect(jsonPath("$.name", is(sampleBill.getName())))
                .andExpect(jsonPath("$.amount", is(sampleBill.getAmount())))
                .andExpect(jsonPath("$.dueDate", is(sampleBill.getDueDate().toString())));
    }

    @Test
    void testGetBillById() throws Exception {
        // Add a bill first
        mockMvc.perform(post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill)))
                .andExpect(status().isOk());

        // Retrieve the bill by ID
        mockMvc.perform(get("/bills/{id}", sampleBill.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleBill.getId())))
                .andExpect(jsonPath("$.name", is(sampleBill.getName())))
                .andExpect(jsonPath("$.amount", is(sampleBill.getAmount())))
                .andExpect(jsonPath("$.dueDate", is(sampleBill.getDueDate().toString())));
    }

    @Test
    void testDeleteBill() throws Exception {
        // Add a bill first
        mockMvc.perform(post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill)))
                .andExpect(status().isOk());

        // Delete the bill
        mockMvc.perform(delete("/bills/{id}", sampleBill.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Bill deleted successfully!"));

        // Verify the bill is no longer available
        mockMvc.perform(get("/bills/{id}", sampleBill.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBill() throws Exception {
        // Add a bill first
        mockMvc.perform(post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBill)))
                .andExpect(status().isOk());

        // Update the bill
        Bill updatedBill = new Bill(sampleBill.getId(), "Updated Bill", 200.00, LocalDate.of(2024, 1, 1));
        mockMvc.perform(put("/bills/{id}", sampleBill.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBill)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bill updated successfully!"));

        // Verify the updated bill details
        mockMvc.perform(get("/bills/{id}", sampleBill.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedBill.getId())))
                .andExpect(jsonPath("$.name", is(updatedBill.getName())))
                .andExpect(jsonPath("$.amount", is(updatedBill.getAmount())))
                .andExpect(jsonPath("$.dueDate", is(updatedBill.getDueDate().toString())));
    }
}
