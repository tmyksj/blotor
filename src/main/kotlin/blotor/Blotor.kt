package blotor

import blotor.command.root.RootCommand
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Blotor

fun main(args: Array<String>) {
    val logger: Logger = LogManager.getLogger(Blotor::class)

    logger.info("")
    logger.info("b")
    logger.info("b    ll       t")
    logger.info("bbb  ll  oo  ttt  oo  r rr")
    logger.info("b  b ll o  o  t  o  o rr")
    logger.info("bbb  ll  oo   tt  oo  r")
    logger.info("")
    logger.info("")

    try {
        RootCommand.run(args.toList())
        logger.info("done.")
    } catch (e: Error) {
        logger.error("fail.")
    }
}
