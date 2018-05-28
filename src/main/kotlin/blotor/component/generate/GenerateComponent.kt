package blotor.component.generate

import blotor.component.Component
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.time.LocalDateTime
import java.util.*

class GenerateComponent : Component {

    private val logger: Logger = LogManager.getLogger(GenerateComponent::class)

    override fun run() {
        val uuid: String = UUID.randomUUID().toString()
        val currentTime: LocalDateTime = LocalDateTime.now()

        File("docs-src/article/${uuid}").mkdirs()
        File("docs-src/article/${uuid}/index.html").writeText(text = buildString {
            appendln("created: ${currentTime}")
            appendln("modified: ${currentTime}")
            appendln("subject: hello world")
            appendln("tag: tag1, tag2")
            appendln()
            appendln("<h1>hello world</h1>")
        })

        logger.info("generated to docs-src/article/${uuid}")
    }

}
