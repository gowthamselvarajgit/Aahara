package com.aahara.backend.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OneRepMaxServiceTest {

    @Test
    void testEpleyFormula() {
        OneRepMaxService service = new OneRepMaxService();
        
        // Normal case
        assertEquals(133.33, service.calculateEpley1RM(100.0, 10), 0.1);
        
        // 1 Rep case
        assertEquals(100.0, service.calculateEpley1RM(100.0, 1));
        
        // Edge cases
        assertEquals(0.0, service.calculateEpley1RM(100.0, 0));
        assertEquals(0.0, service.calculateEpley1RM(0.0, 10));
        assertEquals(0.0, service.calculateEpley1RM(-50.0, 5));
    }
}
