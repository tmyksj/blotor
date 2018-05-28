package blotor.data

import java.io.File
import java.time.LocalDateTime

data class Article(
        val uuid: String,
        val created: LocalDateTime,
        val modified: LocalDateTime,
        val subject: String,
        val index: File
)
