package blotor.command.root.build

import blotor.data.Article
import blotor.data.ArticleStaticFile
import blotor.data.ArticleTag
import blotor.data.Tag
import blotor.property.ApplicationProperty
import blotor.template.TemplateSupport
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver
import java.io.File
import java.time.LocalDateTime
import java.util.*

class Builder {

    private val logger: Logger = LogManager.getLogger(Builder::class)

    private lateinit var processTemplate: (String, String, Map<String, Any>) -> Unit

    private lateinit var articleList: List<Article>
    private lateinit var articleStaticFileList: List<ArticleStaticFile>
    private lateinit var articleTagList: List<ArticleTag>
    private lateinit var tagList: List<Tag>

    fun build() {
        verify()
        initialize()
        load()
        clean()
        compile()
        logger.info("build: done.")
    }

    private fun compile() {
        compile404()
        compileArticle()
        compileArticleList()
        compileArticleStaticFileList()
        compileTemplateStaticFileList()
        logger.info("compile: done.")
    }

    private fun compile404() {
        processTemplate("404", "${ApplicationProperty.buildDir.path}/404.html", mapOf())
        logger.info("compile 404: done.")
    }

    private fun compileArticle() {
        articleList.forEach { article ->
            File("${ApplicationProperty.buildDir.path}/article/${article.uuid}").mkdirs()
            processTemplate("article", "${ApplicationProperty.buildDir.path}/article/${article.uuid}/index.html", mapOf(
                    "article" to article
            ))
        }

        logger.info("compile article: done.")
    }

    private fun compileArticleList() {
        val compileList: (list: List<Article>, prefix: String) -> Unit = { list, prefix ->
            val chunkedArticleList: List<List<Article>> = list.chunked(ApplicationProperty.page)

            chunkedArticleList.forEachIndexed { index, articleList ->
                File("${ApplicationProperty.buildDir.path}/${prefix}page/${index + 1}").mkdirs()
                processTemplate("article-list",
                        "${ApplicationProperty.buildDir.path}/${prefix}page/${index + 1}/index.html",
                        mapOf(
                                "articleList" to articleList,
                                "currentPage" to index + 1,
                                "lastPage" to chunkedArticleList.size,
                                "pageUrlPrefix" to "${prefix}page/"
                        ))
            }

            File("${ApplicationProperty.buildDir.path}/${prefix}page/1/index.html")
                    .copyTo(File("${ApplicationProperty.buildDir.path}/${prefix}index.html"))
        }

        compileList(articleList.sortedByDescending { it.created }, "")

        tagList.forEach { tag ->
            compileList(articleTagList.filter { it.tag == tag }.map { it.article }.sortedByDescending { it.created },
                    "tag/${tag.value}/")
        }

        logger.info("compile article list: done.")
    }

    private fun compileArticleStaticFileList() {
        articleStaticFileList.forEach {
            it.staticFile.copyTo(
                    File("${ApplicationProperty.buildDir}/article/${it.article.uuid}/${it.staticFile.name}"))
        }

        logger.info("compile article static file list: done.")
    }

    private fun compileTemplateStaticFileList() {
        File("${ApplicationProperty.srcDir.path}/template").listFiles().filter { it.extension != "html" }.forEach {
            it.copyTo(File("${ApplicationProperty.buildDir}/${it.name}"))
        }

        logger.info("compile template static file list: done.")
    }

    private fun clean() {
        ApplicationProperty.buildDir.deleteRecursively()
        ApplicationProperty.buildDir.mkdirs()
        logger.info("clean ${ApplicationProperty.buildDir.path}: done.")
    }

    private fun initialize() {
        initializeTemplateEngine()
        logger.info("initialize: done.")
    }

    private fun initializeTemplateEngine() {
        val templateResolver: FileTemplateResolver = FileTemplateResolver()
        templateResolver.prefix = "${ApplicationProperty.srcDir.path}/template/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML

        val templateEngine = TemplateEngine()
        templateEngine.templateResolvers = setOf(templateResolver)

        processTemplate = { template, result, attribute ->
            val context: Context = Context(Locale.getDefault(), mapOf(
                    "ts" to TemplateSupport(
                            articleList = articleList,
                            articleStaticFileList = articleStaticFileList,
                            articleTagList = articleTagList,
                            tagList = tagList
                    )
            ) + attribute)

            templateEngine.process(template, context, File(result).bufferedWriter())
        }

        logger.info("initialize template engine: done.")
    }

    private fun load() {
        loadData()
        logger.info("load: done.")
    }

    private fun loadData() {
        val loadIndex: (File) -> File = {
            it.listFiles().first { it.name.matches(Regex("^index\\..*")) }
        }

        val loadMeta: (String, File) -> String = { s, index ->
            index.useLines {
                it.takeWhile { it != "" }
                        .filter { it.matches(Regex("^${s}:.*")) }
                        .map { it.substring(startIndex = s.length + 1).trim() }
                        .first()
            }
        }

        val loadMetaTag: (File) -> List<String> = {
            loadMeta("tag", it).split(",").map { it.trim() }.distinct()
        }

        articleList = File("${ApplicationProperty.srcDir.path}/article").listFiles().map {
            val index: File = loadIndex(it)

            Article(
                    uuid = it.name,
                    created = LocalDateTime.parse(loadMeta("created", index)),
                    modified = LocalDateTime.parse(loadMeta("modified", index)),
                    subject = loadMeta("subject", index),
                    index = index
            )
        }

        tagList = articleList.map {
            loadMetaTag(it.index).map { Tag(value = it) }
        }.flatMap { it }.distinct()

        articleStaticFileList = articleList.map { article ->
            File("${ApplicationProperty.srcDir.path}/article/${article.uuid}").listFiles()
                    .filter { it.name != article.index.name }
                    .map { ArticleStaticFile(article = article, staticFile = it) }
        }.flatMap { it }

        articleTagList = articleList.map { article ->
            loadMetaTag(article.index).map { tag ->
                ArticleTag(
                        article = article,
                        tag = tagList.first { it.value == tag }
                )
            }
        }.flatMap { it }

        logger.info("load data: done.")
    }

    private fun verify() {
        logger.warn("src directory verification is a work-in-progress.")
        logger.warn("build process maybe occur unexpected exception.")
        logger.info("verify: done.")
    }

}
