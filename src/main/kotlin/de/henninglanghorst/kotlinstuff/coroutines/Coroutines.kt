package de.henninglanghorst.kotlinstuff.coroutines

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.rx2.await


fun main(args: Array<String>) {

    sequential()

    coroutines()

    Thread.sleep(6000)

}

private fun sequential() {
    val start = System.nanoTime()
    val shoppingCart = ShoppingCart(listOf(ArticleID(), ArticleID(), ArticleID()))

    val articles = shoppingCart.articleIds.map { DataStore.queryArticleByID(it) }
    articles.forEach { println(it) }

    val duration = (System.nanoTime() - start) / 1000000L

    println("duration: $duration ms")
}

private fun coroutines() {

    launch {
        val start = System.nanoTime()
        val shoppingCart = ShoppingCart(listOf(ArticleID(), ArticleID(), ArticleID()))

        val articles = shoppingCart.articleIds.toObservable()
                .flatMap { id ->
                    Observable.defer { Observable.just(DataStore.queryArticleByID(id)) }
                            .subscribeOn(Schedulers.io())
                }
                .toList()
                .await()

        articles.forEach { println(it) }

        val duration = (System.nanoTime() - start) / 1000000L

        println("duration: $duration ms")

    }
}


object DataStore {
    fun queryArticleByID(articleID: ArticleID): Article {
        Thread.sleep(500)
        return Article(articleID, "Name of $articleID")
    }

    suspend fun queryArticleByIDAsync(articleID: ArticleID) = queryArticleByID(articleID)
}


