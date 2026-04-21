# Pokemon Lookup

CS481 Mobile Programming - Spring 2026  
**Team 2**

## Project Description

A simple Android app that allows users to search for Pokemon by name and view their information including sprite, types, and stats. Built using MVVM architecture with Kotlin and PokéAPI integration.

## Features

- Search Pokemon by name
- Display Pokemon sprite image
- Show Pokemon types (Fire, Water, Electric, etc.)
- Show six core stats (HP, Attack, Defense, Special Attack, Special Defense, Speed)
- Loading indicators and error handling

## Tech Stack

- **Language:** Kotlin
- **Min SDK:** API 26 (Android 8.0)
- **Target SDK:** API 36
- **Compile SDK:** API 36
- **Architecture:** MVVM
- **API:** PokéAPI (https://pokeapi.co)

## Libraries

- Retrofit 2.9.0 - HTTP client for API calls
- Gson 2.10.1 - JSON parsing
- Kotlin Coroutines 1.7.3 - Asynchronous operations
- ViewModel & LiveData 2.6.2 - MVVM architecture
- Coil 2.5.0 - Image loading
- Material Design 1.11.0 - UI components

## Team Members

- Member 1: Project Setup & Infrastructure
- Member 2: API Integration
- Member 3: Data Models
- Member 4: Repository Layer
- Member 5: Testing & QA

## Project Structure

app/src/main/java/com/team2/pokemonlookup/
├── data/
│   ├── model/       # Data classes (DTOs)
│   ├── remote/      # Retrofit API service
│   └── repository/  # Repository layer
├── ui/              # UI screens and ViewModels
└── utils/           # Helper utilities

## Current Status

**Sprint 3:** Foundation layer - project setup, API integration, data models

## Setup Instructions

1. Clone the repository:
```bash
   git clone https://github.com/denewyear/poke-tracks.git
```
2. Checkout development branch:
```bash
   cd poke-tracks
   git checkout development
```
3. Open in Android Studio
4. Wait for Gradle sync to complete
5. Build and run on emulator or device

## API Information

Using PokéAPI v2: https://pokeapi.co/api/v2/

Primary endpoint: `GET /pokemon/{name}` - Fetch Pokemon by name or ID