package blotor.command.root.initialize

import blotor.command.Command
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object InitializeCommand : Command {

    private val logger: Logger = LogManager.getLogger(InitializeCommand::class)

    override fun run(args: List<String>) {
        TODO("not implemented")
    }

}
