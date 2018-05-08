package blotor.data

import java.time.LocalDateTime
import java.util.*

data class Article(
        val uuid: UUID,
        val created: LocalDateTime,
        val modified: LocalDateTime,
        val subject: String,
        val tagList: List<Tag>,
        val body: String
)
