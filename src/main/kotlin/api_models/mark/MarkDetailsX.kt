package api_models.mark

import api_models.shared.Mark

data class MarkDetailsX(
    val isFinal: Boolean,
    val isImportant: Boolean,
    val markType: String,
    val markTypeText: String,
    val marks: List<Mark>
)