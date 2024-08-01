package com.example.alarmkotlin

import android.app.TimePickerDialog
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmScreen(
    onCancelClick: () -> Unit,
    onDoneClick: (AlarmData) -> Unit
) {
    val context = LocalContext.current

    // State variables for different alarm settings
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    var repeatMode by remember { mutableStateOf("Never") }
    var selectedDays by remember { mutableStateOf(listOf<String>()) }
    var snoozeDuration by remember { mutableStateOf(5) }
    var alarmLabel by remember { mutableStateOf(TextFieldValue()) }

    val daysOfWeek =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val repeatOptions = listOf("Never", "Daily", "Weekdays", "Weekends", "Custom")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Alarm",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onCancelClick) {
                        Text(text = "Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Handle "Done" action with validation
                        if (alarmLabel.text.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please provide a label for the alarm.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onDoneClick(
                                AlarmData(
                                    time = time,
                                    repeatMode = repeatMode,
                                    selectedDays = selectedDays,
                                    snoozeDuration = snoozeDuration,
                                    alarmLabel = alarmLabel.text
                                )
                            )
                        }
                    }) {
                        Text(text = "Done", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Time Picker
//            TimePicker(
//                selectedTime = time,
//                onTimeSelected = { time = it }
//            )

            TimePicker(
                selectedTime = selectedTime,
                onTimeSelected = { newTime -> selectedTime = newTime }
            )

            // Repeat Mode
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Repeat Mode",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//                repeatOptions.forEach { option ->
//                    RadioButton(
//                        selected = repeatMode == option,
//                        onClick = { repeatMode = option }
//                    )
//                    Text(text = option, modifier = Modifier.padding(end = 8.dp))
//                }
//            }

            Column(modifier = Modifier.fillMaxWidth())
            {
                repeatOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        RadioButton(
                            selected = repeatMode == option,
                            onClick = { repeatMode = option }
                        )
                        Text(text = option, modifier = Modifier.padding(end = 8.dp))
                    }
                }
            }

            // Day Selection (visible only if Custom is selected)
            if (repeatMode == "Custom") {
                Text(
                    text = "Select Days",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                daysOfWeek.forEach { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedDays.contains(day),
                                onClick = {
                                    selectedDays = if (selectedDays.contains(day)) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedDays.contains(day),
                            onCheckedChange = null // handled by Row's onClick
                        )
                        Text(text = day, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            // Snooze Duration
            Text(
                text = "Snooze Duration (minutes)",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Slider(
                value = snoozeDuration.toFloat(),
                onValueChange = { snoozeDuration = it.toInt() },
                valueRange = 1f..30f,
                steps = 29
            )
            Text(text = "$snoozeDuration minutes", style = TextStyle(fontSize = 16.sp))

            // Alarm Label
            Text(
                text = "Alarm Label",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            BasicTextField(
                value = alarmLabel,
                onValueChange = { alarmLabel = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun TimePicker(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current

    // State to hold the formatted time as a string
    val formattedTime = remember { mutableStateOf(selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))) }

    // Click handler for showing the time picker dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            // Update the selected time
            val newTime = LocalTime.of(hour, minute)
            formattedTime.value = newTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            onTimeSelected(newTime)
        },
        selectedTime.hour,
        selectedTime.minute,
        false // Use 12-hour format
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Selected Time: ${formattedTime.value}",
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { timePickerDialog.show() }) {
            Text("Select Time")
        }
    }
}

//@Composable
//fun TimePicker(
//    selectedTime: LocalTime,
//    onTimeSelected: (LocalTime) -> Unit
//) {
//    // For simplicity, using just Text here. In a real-world app, you might use a dialog.
//    val formattedTime =
//        remember { mutableStateOf(selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))) }
//
//    // Replace with a TimePicker dialog in a real app.
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = "Select Time",
//            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = formattedTime.value,
//            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
//            modifier = Modifier.padding(8.dp)
//        )
//        Button(onClick = {
//            // Simulate time change for this example
//            val newTime = selectedTime.plusMinutes(1)
//            formattedTime.value = newTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
//            onTimeSelected(newTime)
//        }) {
//            Text("Change Time")
//        }
//    }
//}

data class AlarmData(
    val time: LocalTime,
    val repeatMode: String,
    val selectedDays: List<String>,
    val snoozeDuration: Int,
    val alarmLabel: String
)

@Preview(showBackground = true)
@Composable
fun PreviewCreateAlarm() {
    CreateAlarmScreen(
        onCancelClick = { /* Handle cancel action */ },
        onDoneClick = { alarmData ->
            // Handle done action with alarm data
        }
    )
}
