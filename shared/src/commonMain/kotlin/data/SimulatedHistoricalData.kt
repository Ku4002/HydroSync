package com.example.hydrosync.data

import com.example.hydrosync.model.Plant
import com.example.hydrosync.model.WateringLog

val plants = listOf(
    Plant(
        id = 1,
        name = "Tomato",
        moisture = 42,
        lastWatered = "Today, 4:40 PM",
        icon = "🍅"
    )
)

val wateringLogs = listOf(
    WateringLog("Tomato", "Today, 4:40 PM", 2, 32, 44, 0.0),
    WateringLog("Tomato", "Today, 2:10 PM", 2, 35, 47, 2.5),
    WateringLog("Tomato", "Today, 11:50 AM", 2, 38, 50, 4.83),
    WateringLog("Tomato", "Today, 9:00 AM", 2, 34, 46, 7.67),
    WateringLog("Tomato", "Yesterday, 6:40 PM", 2, 36, 48, 22.0),
    WateringLog("Tomato", "Yesterday, 3:50 PM", 2, 33, 45, 24.83),
    WateringLog("Tomato", "Yesterday, 2:00 PM", 2, 37, 49, 26.67),
    WateringLog("Tomato", "Yesterday, 1:40 PM", 2, 35, 47, 27.0),
    WateringLog("Tomato", "Yesterday, 1:30 PM", 2, 39, 51, 27.17)
)
