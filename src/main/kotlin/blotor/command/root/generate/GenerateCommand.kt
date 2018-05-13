package blotor.command.root.generate

import blotor.command.Command
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.time.LocalDateTime
import java.util.*

object GenerateCommand : Command {

    private val logger: Logger = LogManager.getLogger(GenerateCommand::class)

    override fun run(args: List<String>) {
        val uuid: String = UUID.randomUUID().toString()
        val currentTime: LocalDateTime = LocalDateTime.now()

        File("docs-src/article/${uuid}").mkdirs()
        File("docs-src/article/${uuid}/index.md").writeText(text = buildString {
            appendln("created: ${currentTime}")
            appendln("modified: ${currentTime}")
            appendln("subject: hello world")
            appendln("tag: blog, hello world")
            appendln()
            appendln("# hello world")
        })

        logger.info("generated to docs-src/article/${uuid}")
    }

}
