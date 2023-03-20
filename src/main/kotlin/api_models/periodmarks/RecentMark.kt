package api_models.periodmarks

import api_models.shared.Mark

data class RecentMark(
    val date: Int,
    val isNew: Boolean,
    val marks: List<Mark>
)