package blotor.command.root.generate

import blotor.command.Command
import blotor.component.generate.GenerateComponent

object GenerateCommand : Command {

    override fun run(args: List<String>) {
        GenerateComponent().run()
    }

}
