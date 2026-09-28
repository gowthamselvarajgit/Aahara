package com.aahara.backend.util;

public class StringNormalizer {
    /**
     * Normalizes a food name or search query for deterministic matching.
     * Trims, lowercases, and removes extra whitespace.
     */
    public static String normalize(String input) {
        if (input == null) return "";
        return input.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
