package com.behere.loc_based_reminder.data.response

import com.behere.location_based_reminder.remote.response.Body
import com.behere.location_based_reminder.remote.response.Header

data class Response(
    val header: Header,
    val body: Body

)