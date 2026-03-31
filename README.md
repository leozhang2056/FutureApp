# FutureApp

**UI is a response, not a screen.**

FutureApp is an intent-driven mobile interaction prototype for the AI-native era.  
Instead of navigating fixed pages across multiple apps, users express goals.  
An on-device AI agent interprets intent, fetches data/tools, and composes dynamic UI (or speaks results directly) from a structured UI recipe.

---

## Why FutureApp

Traditional app UX is page-centric:

- User finds the right app
- User finds the right page
- User performs step-by-step operations

FutureApp explores a new model:

- User states intent
- Agent plans and gathers context
- Client dynamically assembles just-in-time UI for the current task
- For simple tasks, no visible UI is required (voice-first / ambient response)

---

## Core Concept

`Intent -> On-device Agent -> UI Recipe -> Dynamic Renderer -> Action / TTS`

1. **Intent Input**  
   Text or voice input from user.
2. **Agent Reasoning (on-device first)**  
   Local model parses intent, identifies required tools/data, and outputs a normalized recipe.
3. **UI Recipe Protocol**  
   JSON schema describing layout, components, data payload, and actions.
4. **Dynamic UI Composition**  
   Client renderer (Jetpack Compose) maps recipe to composable components.
5. **Action + Voice Output**  
   Buttons trigger system/app actions; TTS can summarize key results.

---

## Architecture (MVP)

### 1) Controller Layer (Agent)
- Intent parsing
- Tool routing
- Recipe generation

### 2) Skill/Data Hub
- Local device capabilities
- External APIs (weather, map, reminders, etc.)
- Unified tool response format

### 3) UI Composer
- Component registry (`WeatherCard`, `RouteCard`, `ReminderCard`, ...)
- Runtime layout composition
- State and action binding

### 4) Output Layer
- Visual UI for complex decisions
- Voice output for concise summaries / hands-free use

---

## Example Recipe

```json
{
  "layout": "vertical_stack",
  "components": [
    {
      "type": "weather_hero",
      "data": { "city": "Auckland", "temp": "18C", "condition": "Rainy" }
    },
    {
      "type": "reminder_card",
      "data": { "text": "Bring an umbrella tomorrow", "time": "08:00" },
      "action": "CREATE_NOTIFICATION"
    },
    {
      "type": "tts_directive",
      "text": "Tomorrow is rainy in Auckland. Reminder created."
    }
  ]
}
```

---

## Project Goals

- Build a working prototype of **Generative UI on mobile**
- Validate **intent-first interaction** over page-first interaction
- Demonstrate **on-device AI orchestration** with privacy-first design
- Provide a reusable baseline for agent-driven Android clients

---

## Tech Direction

- **Client:** Android + Jetpack Compose
- **AI Runtime:** on-device small LLM first (cloud fallback optional)
- **Protocol:** JSON-based UI Recipe (schema versioned)
- **Execution:** action dispatcher for system/app intents
- **Voice:** Android TTS for concise natural output

---

## Milestones

### Phase 0 - Foundation
- [ ] Define recipe schema (`v0`)
- [ ] Build component registry
- [ ] Implement renderer + mock data flow

### Phase 1 - End-to-End MVP
- [ ] Intent input (text first, voice optional)
- [ ] Recipe generation from prompt template / local model
- [ ] 3-5 dynamic components
- [ ] Action dispatch + basic TTS

### Phase 2 - Agentic Workflows
- [ ] Multi-step task planning
- [ ] Tool chaining across domains
- [ ] Personal context memory (local-first)

### Phase 3 - Product Hardening
- [ ] Privacy & permission boundary model
- [ ] Error recovery / fallback surfaces
- [ ] Performance tuning for low-latency rendering

---

## Initial Repo Structure (proposed)

```text
FutureApp/
  android-app/      # Compose renderer and runtime shell
  core-proto/       # Recipe schema and protocol contracts
  agent-logic/      # Intent parsing and recipe generation
  docs/             # Architecture notes and design decisions
```

---

## Quick Start (next step)

1. Scaffold Android app shell with Compose
2. Add a static JSON recipe loader
3. Render recipe with 2-3 components
4. Wire one action + one TTS directive
5. Replace static recipe with model-generated recipe

---

## Vision

In FutureApp, users do not open apps first.  
They express intent first.

The interface becomes temporary, contextual, and generated at runtime.

