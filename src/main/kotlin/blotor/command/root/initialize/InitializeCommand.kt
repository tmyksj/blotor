package blotor.command.root.initialize

import blotor.command.Command
import blotor.component.initialize.InitializeComponent

object InitializeCommand : Command {

    override fun run(args: List<String>) {
        InitializeComponent().run()
    }

}
