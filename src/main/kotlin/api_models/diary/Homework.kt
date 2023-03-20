package api_models.diary

import api_models.shared.File

data class Homework(
    val attachments: List<File>,
    val isCompleted: Boolean,
    val text: String,
    val workIsAttachRequired: Boolean
)