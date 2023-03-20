package api_models.periodmarks

import api_models.shared.RatingShared

data class PeriodMarksResponse(
    val askTeacher: Any?,
    val description: String,
    val mobileSubscriptionStatus: String,
    val periodMarks: List<PeriodMark>,
    val periodNumber: Int,
    val rating: RatingShared,
    val type: String
)