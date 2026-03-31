package com.futureapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.futureapp.core.agent.AgentEngine
import com.futureapp.core.model.IntentRequest
import com.futureapp.core.model.UiComponent
import com.futureapp.core.model.UiRecipe
import com.futureapp.core.runtime.ActionDispatcher
import com.futureapp.core.tools.ReminderTool
import com.futureapp.core.tools.ToolRegistry
import com.futureapp.core.tools.WeatherTool

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val agent = AgentEngine(
            ToolRegistry(
                tools = listOf(
                    WeatherTool(),
                    ReminderTool()
                )
            )
        )
        val dispatcher = ActionDispatcher()

        setContent {
            MaterialTheme {
                FutureAppScreen(
                    onRunIntent = { query -> agent.handle(IntentRequest(query)) },
                    onAction = { action ->
                        Toast.makeText(this, dispatcher.dispatch(action), Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
private fun FutureAppScreen(
    onRunIntent: (String) -> UiRecipe,
    onAction: (String) -> Unit
) {
    var query by remember {
        mutableStateOf("I want weather in Auckland and remind me to bring umbrella")
    }
    var recipe by remember { mutableStateOf<UiRecipe?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("FutureApp - Agent First Client", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Describe your goal") }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { recipe = onRunIntent(query) }) {
                    Text("Run Intent")
                }
            }
            recipe?.let {
                Text(it.title, style = MaterialTheme.typography.titleMedium)
                DynamicSurface(recipe = it, onAction = onAction)
                it.ttsSummary?.let { summary ->
                    Text("TTS: $summary", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun DynamicSurface(
    recipe: UiRecipe,
    onAction: (String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(recipe.components) { component ->
            when (component) {
                is UiComponent.TextBlock -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = component.text,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                is UiComponent.WeatherCard -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Weather", style = MaterialTheme.typography.titleSmall)
                            Text("${component.city} - ${component.temp}")
                            Text(component.condition)
                        }
                    }
                }

                is UiComponent.ReminderCard -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Reminder", style = MaterialTheme.typography.titleSmall)
                            Text(component.text)
                            Text("Time: ${component.time}")
                        }
                    }
                }

                is UiComponent.ActionButton -> {
                    Button(onClick = { onAction(component.action) }) {
                        Text(component.label)
                    }
                }
            }
        }
    }
}
