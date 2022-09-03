package com.example.lunchwallet.util

const val BASE_URL = "https://deca-meal-wallet.herokuapp.com/"

const val ONBOARDING = "Onboarding screens"

const val FINISHED = "Onboarding finished"

const val BENEFICIARY = "Beneficiary"
const val KITCHENSTAFF = "KitchenStaff"
const val ADMIN = "Admin"

const val USER_DATASTORE = "User Datastore"

val BENEFICIARY_EMAIL_PATTERN = Regex("(^[a-z]+\\.+[a-z]+?@decagon.dev)\$")
val ADMIN_EMAIL_PATTERN = Regex("(^[a-z]+?@decagonhq.com)\$")

const val AUTHORIZATION = "Authorization"
const val TOKEN_TYPE = "Bearer"

const val BRUNCH = "BRUNCH"
const val DINNER = "DINNER"
const val SERVING = "SERVING"
const val NOT_SERVING = "NOT SERVING"
const val SERVED = "SERVED"


@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1




