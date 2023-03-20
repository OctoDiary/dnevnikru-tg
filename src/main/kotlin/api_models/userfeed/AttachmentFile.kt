package api_models.userfeed

data class AttachmentFile(
    val extension: String,
    val fileId: String,
    val fileLink: String,
    val fileName: String
)