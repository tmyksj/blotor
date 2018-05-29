package blotor.command.root.install

import blotor.command.Command
import blotor.command.CommandException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

class InstallCommand : Command {

    private val logger: Logger = LogManager.getLogger(InstallCommand::class)

    override fun run(args: List<String>) {
        if (args.isEmpty()) {
            logger.error("install path is required.")
            throw CommandException()
        }

        listOf(
                "gradle",
                "src",
                ".gitignore",
                "build.gradle",
                "gradlew",
                "gradlew.bat",
                "settings.gradle"
        ).forEach {
            File("${args[0]}/${it}").deleteRecursively()
            File(it).copyRecursively(target = File("${args[0]}/${it}"))
        }

        logger.info("installed to ${args[0]}.")
    }

}
