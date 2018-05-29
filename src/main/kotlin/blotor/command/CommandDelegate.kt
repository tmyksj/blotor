package blotor.command

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class CommandDelegate

fun delegate(args: List<String>, commandMap: Map<String?, Command>) {
    val logger: Logger = LogManager.getLogger(CommandDelegate::class)

    val command: Command? = commandMap[args.getOrNull(0)]

    if (command == null) {
        logger.error("command not found.")
        throw CommandException()
    }

    if (args.isEmpty()) {
        command.run(args)
    } else {
        command.run(args.subList(fromIndex = 1, toIndex = args.size))
    }
}
