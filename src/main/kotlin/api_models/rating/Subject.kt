package api_models.rating

data class Subject(
    val id: Long,
    val name: String,
    val knowledgeArea: String,
    val subjectMood: String
)