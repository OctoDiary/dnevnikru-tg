package models.periodmarks

@Suppress("unused")
enum class PeriodType(val periodTypeName: String) {
    HalfYear("полугодие"),
    Quarter("четверть"),
    Semester("семестр"),
    Trimester("триместр"),
    Module("модуль")
}