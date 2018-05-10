package blotor

import blotor.command.root.RootCommand
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Blotor

fun main(args: Array<String>) {
    val logger: Logger = LogManager.getLogger(Blotor::class)
    logger.info("blotor -- a blog generator")

    try {
        RootCommand.run(args.toList())
        logger.info("done.")
    } catch (e: Throwable) {
        logger.error("fail.")
    }
}
