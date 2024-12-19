package com.example.billstracker;

import org.springframework.http.ResponseEntity; // Import to handle HTTP responses
import org.springframework.web.bind.annotation.*; // Import for REST controller annotations

import java.time.LocalDate; // Import to handle date types
import java.util.ArrayList; // Import for list implementation
import java.util.List; // Import for List interface
import java.util.Optional; // Import for handling optional values
import java.util.concurrent.atomic.AtomicInteger; // Import for thread-safe ID generation

/**
 * REST Controller for managing bill resources.
 */
@RestController
@RequestMapping("/bills") // Base URL for all endpoints in this controller
public class BillController {

    // In-memory list to store bills
    private final List<Bill> bills = new ArrayList<>();
    // Thread-safe counter for generating unique IDs
    private final AtomicInteger currentId = new AtomicInteger(1);

    /**
     * GET endpoint to retrieve all bills.
     *
     * @return List of all bills.
     */
    @GetMapping
    public List<Bill> getAllBills() {
        // Return the current list of bills
        return bills;
    }

    /**
     * POST endpoint to add a new bill.
     *
     * @param bill The bill object sent in the request body.
     * @return ResponseEntity containing the created bill with a unique ID.
     */
    @PostMapping
    public ResponseEntity<Bill> addBill(@RequestBody Bill bill) {
        // Assign a unique ID to the bill
        bill.setId(currentId.getAndIncrement());
        // Add the bill to the list
        bills.add(bill);
        // Return the created bill as a response
        return ResponseEntity.ok(bill);
    }

    /**
     * GET endpoint to retrieve a bill by its ID.
     *
     * @param id The ID of the bill to retrieve.
     * @return ResponseEntity containing the bill if found, or a 404 response if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable int id) {
        // Find the bill with the specified ID in the list
        Optional<Bill> bill = bills.stream().filter(b -> b.getId() == id).findFirst();
        // If found, return the bill; otherwise, return a 404 response
        return bill.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE endpoint to delete a bill by its ID.
     *
     * @param id The ID of the bill to delete.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable int id) {
        // Attempt to remove the bill with the specified ID
        boolean removed = bills.removeIf(b -> b.getId() == id);
        if (removed) {
            // If the bill was removed, return a success message
            return ResponseEntity.ok("Bill deleted successfully!");
        } else {
            // If no bill was found, return a 404 response
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT endpoint to update an existing bill.
     *
     * @param id          The ID of the bill to update.
     * @param updatedBill The updated bill object sent in the request body.
     * @return ResponseEntity indicating success or failure.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBill(@PathVariable int id, @RequestBody Bill updatedBill) {
        // Find the existing bill with the specified ID
        Optional<Bill> existingBill = bills.stream().filter(b -> b.getId() == id).findFirst();
        if (existingBill.isPresent()) {
            // If the bill exists, update its fields
            Bill bill = existingBill.get();
            bill.setName(updatedBill.getName());
            bill.setAmount(updatedBill.getAmount());

            // Parse the incoming date string to LocalDate
            LocalDate newDueDate = LocalDate.parse(updatedBill.getDueDate().toString());
            bill.setDueDate(newDueDate);

            return ResponseEntity.ok("Bill updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


