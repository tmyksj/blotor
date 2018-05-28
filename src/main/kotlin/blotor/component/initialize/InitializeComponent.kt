package blotor.component.initialize

import blotor.component.Component
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

class InitializeComponent : Component {

    private val logger: Logger = LogManager.getLogger(InitializeComponent::class)

    override fun run() {
        val srcDir: File = File(ClassLoader.getSystemResource("docs-src").toURI())
        val destDir: File = File("docs-src")

        srcDir.copyRecursively(target = destDir, overwrite = false, onError = { file, _ ->
            logger.warn("skipped ${file.path}")
            OnErrorAction.SKIP
        })

        logger.info("initialized blog.")
    }

}
