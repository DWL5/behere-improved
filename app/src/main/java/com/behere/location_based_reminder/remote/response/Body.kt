package com.behere.location_based_reminder.remote.response

import com.behere.loc_based_reminder.data.response.Item

data class Body(
    val items: List<Item>,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)