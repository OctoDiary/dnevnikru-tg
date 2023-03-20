package api_models.user

data class ReportingPeriodGroup(
    val id: Long,
    val type: String,
    val periods: List<Periods>
)