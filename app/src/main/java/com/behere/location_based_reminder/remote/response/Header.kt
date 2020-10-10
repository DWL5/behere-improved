package com.behere.location_based_reminder.remote.response

data class Header(
    val description: String,
    val columns: List<String>,
    val stdrYm: String,
    val resultCode: String,
    val resultMsg: String
)