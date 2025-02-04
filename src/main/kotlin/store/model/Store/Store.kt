package store.model.Store

import store.model.Buy.BuyController
import store.model.Product.Product
import store.Util.readFile
import store.View.InputView
import store.View.OutputView
import store.model.Promotion.Promotion

class Store {
    private val inputView = InputView()
    private val outputView = OutputView()
    private lateinit var products: List<Product>
    private lateinit var promotions: List<Promotion>

    private fun getProducts(): List<Product> {
        val resultProducts = readFile("products.md").map {
            val (name, price, quantity, promotionInput) = it

            Product(name, price.toInt(), quantity.toInt(), promotions.find { promotionItem ->
                promotionItem.getName() == promotionInput
            })
        }.toMutableList()

        // 일반 상품이 없는 경우 추가
        resultProducts.filter { product ->
            resultProducts.find { it.getName() == product.getName() && it.getPromotion() == null } == null
        }.forEach {
            resultProducts.add(resultProducts.indexOf(it) + 1, Product(it.getName(), it.getPrice(), 0, null))
        }

        return resultProducts.toList()
    }

    private fun getPromotion(): List<Promotion> {
        return readFile("promotions.md").map {
            val (name, buy, get, start_date, end_date) = it
            Promotion(name, buy.toInt(), get.toInt(), start_date, end_date)
        }
    }


    private fun saveStock() {
        promotions = getPromotion()
        products = getProducts()
    }


    fun open() {
        saveStock()

        while (true) {
            val buyController = BuyController(inputView, outputView, promotions, products)
            buyController.buyStart()
            if (!inputView.isReBuy()) break
        }
    }

}