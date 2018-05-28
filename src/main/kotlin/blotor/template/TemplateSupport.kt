package blotor.template

import blotor.data.Article
import blotor.data.ArticleStaticFile
import blotor.data.ArticleTag
import blotor.data.Tag
import blotor.property.ApplicationProperty

class TemplateSupport(
        private val articleList: List<Article>,
        private val articleStaticFileList: List<ArticleStaticFile>,
        private val articleTagList: List<ArticleTag>,
        private val tagList: List<Tag>
) {

    fun articleList(): List<Article> = articleList
            .sortedBy { it.created }

    fun articleStaticFileList(): List<ArticleStaticFile> = articleStaticFileList

    fun articleTagList(): List<ArticleTag> = articleTagList

    fun readArticleIndex(article: Article): String = article
            .index
            .useLines { it.dropWhile { it != "" }.drop(1).joinToString(separator = " ") }

    fun recentArticleList(): List<Article> = articleList
            .sortedByDescending { it.created }
            .subList(0, minOf(ApplicationProperty.page, articleList.size))

    fun tagList(): List<Tag> = tagList
            .sortedBy { it.value }

    fun tagList(article: Article): List<Tag> = articleTagList
            .filter { it.article == article }
            .map { it.tag }
            .sortedBy { it.value }

}
