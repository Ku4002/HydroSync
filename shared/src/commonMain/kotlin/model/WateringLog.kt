package com.example.hydrosync.model

data class WateringLog(
    val plantName: String,
    val time: String,
    val duration: Int,
    val moistureBefore: Int,
    val moistureAfter: Int,
    val hoursAgo: Double
)
