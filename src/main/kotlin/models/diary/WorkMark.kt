package models.diary

import models.shared.Mark

data class WorkMark(
    val marks: List<Mark>,
    val workId: Long
)