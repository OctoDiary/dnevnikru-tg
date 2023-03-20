package api_models.user

import api_models.periodmarks.PeriodType

data class Periods(
    val id: Long,
    val number: Int,
    val type: PeriodType,
    val studyYear: Int,
    val isCurrent: Boolean,
    val dateStart: Long,
    val dateFinish: Long
)