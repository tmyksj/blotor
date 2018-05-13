package blotor.data

import java.time.LocalDateTime

data class Article(
        val uuid: String,
        val created: LocalDateTime,
        val modified: LocalDateTime,
        val subject: String,
        val tagList: List<Tag>,
        val body: String
)
