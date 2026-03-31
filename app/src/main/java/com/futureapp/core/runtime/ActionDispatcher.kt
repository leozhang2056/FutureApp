package com.futureapp.core.runtime

class ActionDispatcher {
    fun dispatch(action: String): String {
        return when (action) {
            "CREATE_REMINDER" -> "Reminder created (prototype action)."
            else -> "Unknown action: $action"
        }
    }
}
