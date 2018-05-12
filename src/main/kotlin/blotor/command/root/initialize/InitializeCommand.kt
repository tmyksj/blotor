package blotor.command.root.initialize

import blotor.command.Command
import blotor.exception.CommandException
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object InitializeCommand : Command {

    private val logger: Logger = LogManager.getLogger(InitializeCommand::class)

    override fun run(args: List<String>) {
        logger.info("initialize blog.")

        val srcDir: File = File(ClassLoader.getSystemResource("docs.src").toURI())
        val destDir: File = File("docs.src")

        if (destDir.exists()) {
            logger.error("docs.src is already exists.")
            throw CommandException()
        }

        FileUtils.copyDirectory(srcDir, destDir)
        logger.info("done.")
    }

}
