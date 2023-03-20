package api_models.user

data class School(
    val id: Long,
    val name: String,
    val type: String,
    val avatarUrl: String
)