package api_models.userfeed

data class ChildLocationInfo(
    val code: Int,
    val context: Context,
    val lastLocation: Any?,
    val personId: Long,
    val zones: List<Zone>
)