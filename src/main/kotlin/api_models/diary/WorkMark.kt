package api_models.diary

import api_models.shared.Mark

data class WorkMark(
    val marks: List<Mark>,
    val workId: Long
)