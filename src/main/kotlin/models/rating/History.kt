package models.rating

data class History(
    val rankingPosition: RankingPosition?,
    val historyItems: List<HistoryItems>
)