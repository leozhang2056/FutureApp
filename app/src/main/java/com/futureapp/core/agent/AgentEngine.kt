package com.futureapp.core.agent

import com.futureapp.core.model.ExecutionPlan
import com.futureapp.core.model.IntentRequest
import com.futureapp.core.model.PlanStep
import com.futureapp.core.model.ToolResult
import com.futureapp.core.model.UiComponent
import com.futureapp.core.model.UiRecipe
import com.futureapp.core.tools.ToolRegistry

class AgentEngine(private val registry: ToolRegistry) {
    fun handle(request: IntentRequest): UiRecipe {
        val plan = buildPlan(request)
        val results = executePlan(plan)
        return composeRecipe(request, results)
    }

    private fun buildPlan(request: IntentRequest): ExecutionPlan {
        val lower = request.query.lowercase()
        val city = if ("auckland" in lower) "Auckland" else "Auckland"

        return ExecutionPlan(
            goal = request.query,
            steps = listOf(
                PlanStep(toolName = "weather", params = mapOf("city" to city)),
                PlanStep(
                    toolName = "reminder",
                    params = mapOf("text" to "Bring an umbrella tomorrow", "time" to "08:00")
                )
            )
        )
    }

    private fun executePlan(plan: ExecutionPlan): List<ToolResult> {
        return plan.steps.map { step -> registry.execute(step) }
    }

    private fun composeRecipe(request: IntentRequest, results: List<ToolResult>): UiRecipe {
        val weather = results.firstOrNull { it.toolName == "weather" }?.data.orEmpty()
        val reminder = results.firstOrNull { it.toolName == "reminder" }?.data.orEmpty()

        val weatherCard = UiComponent.WeatherCard(
            city = weather["city"].orEmpty().ifBlank { "Unknown City" },
            temp = weather["temp"].orEmpty().ifBlank { "--" },
            condition = weather["condition"].orEmpty().ifBlank { "Unknown" }
        )

        val reminderCard = UiComponent.ReminderCard(
            text = reminder["text"].orEmpty().ifBlank { "No reminder" },
            time = reminder["time"].orEmpty().ifBlank { "--:--" }
        )

        return UiRecipe(
            title = "FutureApp Dynamic Surface",
            components = listOf(
                UiComponent.TextBlock("Intent: ${request.query}"),
                weatherCard,
                reminderCard,
                UiComponent.ActionButton(label = "Create Reminder", action = "CREATE_REMINDER")
            ),
            ttsSummary = "Tomorrow is rainy. Reminder is ready."
        )
    }
}
