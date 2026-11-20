package com.fmtech.fmcommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void shouldReturnWelcomeMessage() {

        // Act
        String result = homeController.home();

        // Assert
        assertEquals(
            "Welcome to FM-Commerce API!",
            result
        );
    }

    @Test
    void shouldReturnHealthMessage() {

        // Act
        String result = homeController.health();

        // Assert
        assertEquals(
            "FM-Commerce API is running!",
            result
        );
    }
}

