@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.alarmkotlin

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(onAddAlarmClick: () -> Unit) {
    // Sample list of alarms
    val alarms = remember {
        mutableStateListOf(
            Pair("3:00 am", "Once"),
            Pair("6:00 am", "Repeat"),
            Pair("3:00 am", "Once"),
            Pair("6:00 am", "Repeat"),
            Pair("3:00 am", "Once"),
            Pair("6:00 am", "Repeat"),
            Pair("3:00 am", "Once"),
            Pair("6:00 am", "Repeat"),
            Pair("3:00 am", "Once"),
            Pair("6:00 am", "Repeat"),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Alarms",
                        color = Color.Blue
                    )
                },
                actions = {
                    IconButton(onClick = { onAddAlarmClick() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Alarm"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (alarms.isEmpty()) {
                Text(
                    text = "No alarm set.",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(alarms) { alarm ->
                        AlarmListItem(time = alarm.first, status = alarm.second)
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmListItem(time: String, status: String) {
    var switchState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time,
                    color = Color.Black,
                    fontSize = 24.sp
                )
                Switch(
                    checked = switchState,
                    onCheckedChange = { switchState = it }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Now",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    text = status,
                    color = Color.Blue,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(onAddAlarmClick = {})
}