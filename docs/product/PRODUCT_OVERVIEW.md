# Aahara Project Overview

This document provides a high‑level overview of the Aahara project, its goals, and the technology stack.

## Vision
Aahara is a premium health, nutrition, weight and strength‑tracking application. It enables users to log food and workouts, calculate nutrition and performance metrics, store a historical record, and visualize progress.

## Core Principles
- **Local‑first**: The mobile app works offline using SQLite and syncs later with the backend.
- **Extensible Architecture**: Designed to evolve without rewriting core components.
- **Privacy‑first**: No unnecessary data collection; all credentials are externalised.

## Stack
- **Mobile**: React Native (TypeScript), React Navigation, Zustand, SQLite.
- **Backend**: Spring Boot (Java), Spring Data JPA, MySQL, Flyway.
- **Infrastructure**: Local development using Docker (optional), MySQL.

The remainder of the repository contains detailed documentation for each area.
