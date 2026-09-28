# Aahara

A simple, premium health, nutrition, weight and strength‑tracking application.

## Overview
Aahara helps users log what they eat and train, calculates nutrition and workout metrics, stores a history, and visualises progress. The project is built as a **monorepo** with:

- **Mobile**: React Native (TypeScript) using React Navigation, Zustand, SQLite.
- **Backend**: Spring Boot (Java) exposing REST APIs, persisting data in MySQL.

The architecture is **local‑first**: the mobile app works offline with a local SQLite DB and syncs later with the backend.

## Repository Structure
```
Aahara/
├─ mobile/               # React Native app
│   └─ src/
├─ backend/              # Spring Boot service
│   └─ src/main/java/... 
├─ docs/                 # Project documentation
├─ .gitignore
├─ .env.example          # Environment variable placeholders
└─ README.md
```

See the `docs/` folder for detailed documentation on architecture, setup, and development guidelines.
