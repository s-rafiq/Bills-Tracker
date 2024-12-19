package com.example.billstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillsTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillsTrackerApplication.class, args);
        System.out.println("Bills Tracker App is running!");
    }
}



