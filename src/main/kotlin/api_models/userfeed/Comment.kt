package api_models.userfeed

data class Comment(
    val author: Author,
    val chatEnabled: Boolean,
    val date: Int,
    val lessonId: Long,
    val text: String
)