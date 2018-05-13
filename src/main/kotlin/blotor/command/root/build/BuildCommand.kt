package blotor.command.root.build

import blotor.command.Command
import blotor.command.CommandException

object BuildCommand : Command {

    override fun run(args: List<String>) {
        try {
            Builder().build()
        } catch (e: BuildErrorException) {
            throw CommandException()
        }
    }

}
