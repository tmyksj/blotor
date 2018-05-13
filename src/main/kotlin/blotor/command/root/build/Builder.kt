package blotor.command.root.build

import blotor.data.Article
import blotor.data.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class Builder(
        private val srcDir: File = File("docs-src"),
        private val buildDir: File = File("docs")
) {

    private val logger: Logger = LogManager.getLogger(Builder::class)

    private lateinit var templateEngine: TemplateEngine

    private lateinit var articleList: List<Article>
    private lateinit var tagList: List<Tag>

    private lateinit var recentArticleList: List<Article>

    private val page: Int = 10

    fun build() {
        verify()

        initializeTemplateEngine()
        loadArticleList()

        clean()

        build404()
        buildArticle()
        buildArticleList()
        buildStaticFiles()
    }

    private fun build404() {
        logger.info("build /404.html")

        val context: Context = Context()
        context.setVariable("recentArticleList", recentArticleList)
        context.setVariable("tagList", tagList)

        File("${buildDir.path}/404.html")
                .writeText(text = templateEngine.process("404", context))
    }

    private fun buildArticle() {
        articleList.forEach {
            logger.info("build /article/${it.uuid}/index.html")

            val context: Context = Context()
            context.setVariable("article", it)
            context.setVariable("recentArticleList", recentArticleList)
            context.setVariable("tagList", tagList)

            File("${buildDir.path}/article/${it.uuid}").mkdirs()
            File("${buildDir.path}/article/${it.uuid}/index.html")
                    .writeText(text = templateEngine.process("article", context))
        }
    }

    private fun buildArticleList() {
        val buildList: (list: List<Article>, prefix: String) -> Unit = { list, prefix ->
            val chunkedArticleList: List<List<Article>> = list.chunked(page)
            chunkedArticleList.forEachIndexed { index: Int, articleList ->
                logger.info("build /${prefix}page/${index + 1}/index.html")

                val context: Context = Context()
                context.setVariable("articleList", articleList)
                context.setVariable("currentPage", index + 1)
                context.setVariable("lastPage", chunkedArticleList.size)
                context.setVariable("recentArticleList", recentArticleList)
                context.setVariable("tagList", tagList)

                File("${buildDir.path}/${prefix}page/${index + 1}/").mkdirs()
                File("${buildDir.path}/${prefix}page/${index + 1}/index.html")
                        .writeText(text = templateEngine.process("article-list", context))
            }

            File("${buildDir.path}/${prefix}page/1/index.html")
                    .copyTo(File("${buildDir.path}/${prefix}index.html"))
        }

        buildList(articleList, "")
        tagList.forEach { buildList(it.articleList, "tag/${it.value}/") }
    }

    private fun buildStaticFiles() {
        logger.info("build static files")
        File("${srcDir.path}/templates/style.css").copyTo(File("${buildDir}/style.css"))
        File("${srcDir.path}/templates/script.js").copyTo(File("${buildDir}/script.js"))
    }

    private fun clean() {
        logger.info("clean ${buildDir.path}")

        buildDir.deleteRecursively()
        buildDir.mkdirs()
    }

    private fun initializeTemplateEngine() {
        logger.info("initialize template engine: done.")

        val templateResolver: FileTemplateResolver = FileTemplateResolver()
        templateResolver.prefix = "${srcDir.path}/templates/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML

        templateEngine = TemplateEngine()
        templateEngine.templateResolvers = setOf(templateResolver)
    }

    private fun loadArticleList() {
        val articleMutableList: MutableList<Article> = mutableListOf()
        val tagMutableMap: MutableMap<String, Tag> = mutableMapOf()

        File("${srcDir.path}/article")
                .listFiles()
                .forEach {
                    try {
                        logger.info("load ${it.name}")

                        val articleSource: File = File("${it.path}/index.md")

                        val lineList: List<String> = articleSource.readLines()
                        val blankLineIndex: Int = lineList.indexOf("")

                        val meta: Map<String, String> = lineList.subList(0, blankLineIndex).associate {
                            val index: Int = it.indexOf(":")
                            Pair(it.substring(0, index).trim(), it.substring(index + 1).trim())
                        }

                        val article: Article = Article(
                                uuid = it.name,
                                created = LocalDateTime.parse(meta["created"]!!),
                                modified = LocalDateTime.parse(meta["modified"]!!),
                                subject = meta["subject"]!!,
                                tagList = meta["tag"]!!
                                        .split(",")
                                        .map { it.trim() }
                                        .distinct()
                                        .map {
                                            if (tagMutableMap[it] == null) {
                                                tagMutableMap[it] = Tag(value = it, articleList = mutableListOf())
                                            }
                                            tagMutableMap[it]!!
                                        },
                                body = lineList
                                        .subList(blankLineIndex + 1, lineList.size)
                                        .joinToString(separator = "<br>")
                        )

                        articleMutableList.add(element = article)
                        article.tagList.forEach {
                            (tagMutableMap[it.value]!!.articleList as MutableList<Article>).add(element = article)
                        }
                    } catch (e: BuildErrorException) {
                        logger.warn("load ${it.name}: fail. ${e.message}")
                    } catch (e: DateTimeParseException) {
                        logger.warn("load ${it.name}: fail. illegal date time format.")
                    } catch (e: KotlinNullPointerException) {
                        logger.warn("load ${it.name}: fail. missing meta.")
                    }
                }

        articleMutableList.sortBy { it.created }
        tagMutableMap.values.forEach { (it.articleList as MutableList<Article>).sortBy { it.created } }

        articleList = articleMutableList.toList()
        tagList = tagMutableMap.values.sortedBy { it.value }

        recentArticleList = articleList.subList(0, minOf(page, articleList.size))
    }

    private fun verify() {
        logger.warn("src directory verification is a work-in-progress.")
        logger.warn("build process maybe occur unexpected exception.")
    }

}
