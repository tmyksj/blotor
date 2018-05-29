package blotor.command.root.initialize

import blotor.command.Command
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

class InitializeCommand : Command {

    private val logger: Logger = LogManager.getLogger(InitializeCommand::class)

    override fun run(args: List<String>) {
        val srcDir: File = File(ClassLoader.getSystemResource("docs-src").toURI())
        val destDir: File = File("docs-src")

        srcDir.copyRecursively(target = destDir, overwrite = false, onError = { file, _ ->
            logger.warn("skipped ${file.path}")
            OnErrorAction.SKIP
        })

        logger.info("initialized blog.")
    }

}
