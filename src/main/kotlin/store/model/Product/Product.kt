package store.model.Product

import store.Enum.Error
import store.model.Promotion.Promotion

// 상품
class Product(
    private var name: String,
    private val price: Int,
    private var quantity: Int,
    private var promotion: Promotion?
) {

    fun getName(): String {
        return name
    }

    fun getPrice(): Int {
        return price
    }

    fun getPromotion(): Promotion? {
        return promotion
    }

    fun getQuantity(): Int {
        return quantity
    }

    fun buy(quantity: Int) {
        checkCanBuyQuantity(quantity)
        this.quantity -= quantity
    }

    fun canBuyQuantity(quantity: Int): Boolean {
        return quantity <= this.quantity
    }

    private fun checkCanBuyQuantity(quantity: Int) {
        if (!canBuyQuantity(quantity)) {
            throw IllegalArgumentException(Error.EXCEED_QUANTITY.errorMessage)
        }
    }
}