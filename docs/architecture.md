# FutureApp Architecture

## Positioning

FutureApp is an agent-first mobile runtime:

- User expresses intent
- Agent plans and orchestrates tools
- Runtime composes dynamic UI from structured recipe
- Action layer executes user-approved operations

## Runtime Flow

1. `IntentRequest` enters Agent Core
2. Agent builds `ExecutionPlan`
3. Tool Orchestrator executes steps and aggregates `ToolResult`
4. Agent emits `UiRecipe`
5. GenUI runtime renders components
6. User triggers action, dispatcher executes operation

## Current Prototype Scope

- Text-based intent input
- Mock weather/reminder tools
- Recipe-driven dynamic cards and action button
- Local action dispatcher stub

## Next Steps

- Replace mock tool data with real API integrations
- Add on-device model adapter
- Add permission and policy guardrails
- Add persistent memory and session state machine
