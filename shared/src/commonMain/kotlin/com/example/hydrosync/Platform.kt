package com.example.hydrosync

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform