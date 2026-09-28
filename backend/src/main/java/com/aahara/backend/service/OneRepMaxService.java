package com.aahara.backend.service;

import org.springframework.stereotype.Service;

@Service
public class OneRepMaxService {

    /**
     * Calculates the estimated 1 Rep Max using the Epley formula.
     * Formula: Weight * (1 + (Reps / 30))
     * 
     * @param weightKg The weight lifted in kg.
     * @param reps The number of repetitions.
     * @return The estimated 1RM. Returns weight if reps is 1. Returns 0 if weight/reps are invalid.
     */
    public double calculateEpley1RM(double weightKg, int reps) {
        if (weightKg <= 0 || reps <= 0) {
            return 0.0;
        }
        if (reps == 1) {
            return weightKg;
        }
        
        return weightKg * (1.0 + (reps / 30.0));
    }
}
