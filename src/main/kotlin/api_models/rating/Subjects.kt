package api_models.rating

data class Subjects(
    val place: Int,
    val subject: Subject,
    val trend: String,
    val averageMark: String
)