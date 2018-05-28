package blotor.command.root.build

import blotor.command.Command
import blotor.component.build.BuildComponent

object BuildCommand : Command {

    override fun run(args: List<String>) {
        BuildComponent().run()
    }

}
