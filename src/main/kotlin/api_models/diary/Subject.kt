package api_models.diary

data class Subject(
    val id: Long,
    val knowledgeArea: String,
    val name: String,
    val subjectMood: Any
)