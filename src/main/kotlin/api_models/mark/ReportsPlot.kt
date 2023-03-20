package api_models.mark

data class ReportsPlot(
    val averageMarks: AverageMarksXX,
    val knowledgeAreaGroup: String,
    val knowledgeAreaGroupName: String,
    val plotOptions: PlotOptions,
    val reportingPeriodsReports: List<ReportingPeriodsReportX>,
    val subject: Subject
)