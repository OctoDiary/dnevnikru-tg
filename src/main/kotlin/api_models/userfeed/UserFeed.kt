package api_models.userfeed

import api_models.shared.RatingShared

data class UserFeed(
    val childLocationInfo: ChildLocationInfo,
    val description: String,
    val feed: List<PeriodMark>,
    val homeworksProgress: Any?,
    val mobileSubscriptionStatus: String,
    val rating: RatingShared,
    val recentMarks: List<RecentMark>,
    val schedule: Schedule,
    val type: FeedType
)