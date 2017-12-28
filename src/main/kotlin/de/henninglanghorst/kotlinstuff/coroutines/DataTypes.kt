package de.henninglanghorst.kotlinstuff.coroutines

import java.util.*

data class ShoppingCart(val articleIds: List<ArticleID> = emptyList()) {
    fun withAddedArticleID(articleID: ArticleID) = ShoppingCart(this.articleIds + articleID)
}

data class Article(val id: ArticleID, val name: String, val desciption: String = "")
data class ArticleID(val values: String = UUID.randomUUID().toString())
