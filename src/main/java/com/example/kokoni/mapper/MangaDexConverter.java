package com.example.kokoni.mapper;

import java.time.LocalDate;
import java.util.Map;

import com.example.kokoni.entity.enums.MediaStatus;

public class MangaDexConverter {

    public String mapDescription(Map<String, String> desc) {
        if (desc == null) return "Sin descripción";
        return desc.getOrDefault("es", desc.getOrDefault("en", "Sin descripción"));
    }

    public LocalDate mapYear(Integer year) {
        return (year != null) ? LocalDate.of(year, 1, 1) : null;
    }

    public MediaStatus mapStatus(String status) {
        if (status == null) return null;
        return switch (status.toLowerCase()) {
            case "ongoing" -> MediaStatus.RELEASING;
            case "completed" -> MediaStatus.FINISHED;
            case "hiatus" -> MediaStatus.HIATUS;
            case "cancelled" -> MediaStatus.CANCELLED;
            default -> null;
        };
    }

    public Integer parseSafeInt(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

