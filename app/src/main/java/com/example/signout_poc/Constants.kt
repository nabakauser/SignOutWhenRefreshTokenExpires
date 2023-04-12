package com.example.signout_poc

object Constants {
    const val KEY_NAME = "keyName"
    const val KEY_PASSWORD = "keyPassword"
    const val SHARED_PREFERENCES = "sharedPreference"

    const val CHANNEL_ID = "channel_id"
    var NOTIFICATION_ID: Int = System.currentTimeMillis().toInt()
    var notificationType: Int? = null

    var EXPIRY_TIME = "expiryTime"
    var FORMATTED_DATE = "formattedDate"


}