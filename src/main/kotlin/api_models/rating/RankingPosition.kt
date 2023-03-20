package api_models.rating

data class RankingPosition(
    val trendDescription: String,
    val placeTrend: String,
    val place: Int?,
    val description: String,
    val backgroundImageUrl: String
)