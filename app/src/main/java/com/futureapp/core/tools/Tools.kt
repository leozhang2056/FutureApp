package com.futureapp.core.tools

import com.futureapp.core.model.PlanStep
import com.futureapp.core.model.ToolResult

interface Tool {
    val name: String
    fun execute(step: PlanStep): ToolResult
}

class WeatherTool : Tool {
    override val name: String = "weather"

    override fun execute(step: PlanStep): ToolResult {
        val city = step.params["city"].orEmpty().ifBlank { "Auckland" }
        return ToolResult(
            toolName = name,
            data = mapOf(
                "city" to city,
                "temp" to "18C",
                "condition" to "Rainy"
            )
        )
    }
}

class ReminderTool : Tool {
    override val name: String = "reminder"

    override fun execute(step: PlanStep): ToolResult {
        val text = step.params["text"].orEmpty().ifBlank { "Bring umbrella" }
        val time = step.params["time"].orEmpty().ifBlank { "08:00" }
        return ToolResult(
            toolName = name,
            data = mapOf(
                "text" to text,
                "time" to time,
                "status" to "ready"
            )
        )
    }
}

class ToolRegistry(tools: List<Tool>) {
    private val toolMap: Map<String, Tool> = tools.associateBy { it.name }

    fun execute(step: PlanStep): ToolResult {
        val tool = requireNotNull(toolMap[step.toolName]) {
            "Tool not found: ${step.toolName}"
        }
        return tool.execute(step)
    }
}
