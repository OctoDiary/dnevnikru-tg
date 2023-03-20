package api_models.shared

data class RatingShared(
    val rankingPlaces: List<RankingPlace>,
    val rankingPosition: RankingPosition
)