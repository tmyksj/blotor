package blotor.command.root

import blotor.command.Command
import blotor.command.delegate
import blotor.command.root.build.BuildCommand
import blotor.command.root.generate.GenerateCommand
import blotor.command.root.initialize.InitializeCommand
import blotor.command.root.install.InstallCommand
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class RootCommand : Command {

    private val logger: Logger = LogManager.getLogger(RootCommand::class)

    override fun run(args: List<String>) {
        logger.info("query: ${args.joinToString()}")

        delegate(args, mapOf(
                "build" to BuildCommand(),
                "generate" to GenerateCommand(),
                "initialize" to InitializeCommand(),
                "install" to InstallCommand()
        ))
    }

}
