package models.userfeed

data class Context(
    val chargeLevel: Int,
    val isBackgroundRefreshEnabled: Boolean,
    val isGeolocationEnabled: Boolean
)