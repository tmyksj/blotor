package blotor.command.root

import blotor.command.Command
import blotor.command.CommandException
import blotor.command.root.build.BuildCommand
import blotor.command.root.generate.GenerateCommand
import blotor.command.root.initialize.InitializeCommand
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object RootCommand : Command {

    private val logger: Logger = LogManager.getLogger(RootCommand::class)

    override fun run(args: List<String>) {
        logger.info("query: " + args.joinToString())

        if (args.isEmpty()) {
            logger.error("illegal query.")
            throw CommandException()
        }

        when (args[0]) {
            "build" -> BuildCommand.run(args.subList(1, args.size))
            "generate" -> GenerateCommand.run(args.subList(1, args.size))
            "initialize" -> InitializeCommand.run(args.subList(1, args.size))
        }
    }

}
