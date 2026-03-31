package com.futureapp.core.model

data class IntentRequest(
    val query: String
)

data class PlanStep(
    val toolName: String,
    val params: Map<String, String>
)

data class ExecutionPlan(
    val goal: String,
    val steps: List<PlanStep>
)

data class ToolResult(
    val toolName: String,
    val data: Map<String, String>
)

data class UiRecipe(
    val title: String,
    val components: List<UiComponent>,
    val ttsSummary: String? = null
)

sealed class UiComponent {
    data class TextBlock(val text: String) : UiComponent()
    data class WeatherCard(
        val city: String,
        val temp: String,
        val condition: String
    ) : UiComponent()

    data class ReminderCard(
        val text: String,
        val time: String
    ) : UiComponent()

    data class ActionButton(
        val label: String,
        val action: String
    ) : UiComponent()
}
