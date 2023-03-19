package models.periodmarks

import models.shared.Mark

data class RecentMark(
    val date: Int,
    val isNew: Boolean,
    val marks: List<Mark>
)