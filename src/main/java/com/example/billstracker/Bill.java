package com.example.billstracker;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

/**
 * Represents a Bill with an ID, name, amount, and due date.
 * Provides JSON serialization/deserialization for date handling.
 */
public class Bill {
    private int id;          // Unique ID for each bill
    private String name;     // Name or description of the bill
    private double amount;   // Amount due for the bill

    // Due date for the bill, formatted and serialized/deserialized for JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dueDate;

    /**
     * Default constructor.
     * Used for creating an empty Bill object.
     */
    public Bill() {
    }

    /**
     * Parameterized constructor to initialize a Bill object.
     *
     * @param id      Unique identifier for the bill
     * @param name    Name or description of the bill
     * @param amount  Monetary value of the bill
     * @param dueDate Due date for the bill
     */
    public Bill(int id, String name, double amount, LocalDate dueDate) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Provides a string representation of the Bill object.
     *
     * @return A string containing all the details of the Bill.
     */
    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                '}';
    }
}
