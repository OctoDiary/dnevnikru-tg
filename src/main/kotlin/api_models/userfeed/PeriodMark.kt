package api_models.userfeed

data class PeriodMark(
    val content: Content,
    val timeStamp: Int,
    val type: FeedType
)