package store.Util

import store.Enum.Error
import store.model.Product.Product
import store.model.Promotion.Promotion

fun findPromotion(promotions : List<Promotion>, promotionName: String): Promotion {
    return promotions.find { it.getName() == promotionName }
        ?: throw IllegalStateException(Error.CANT_FIND_PROMOTION.errorMessage)
}

fun findProduct(products : List<Product>, productName: String): Product {
    return products.find { it.getName() == productName }
        ?: throw IllegalStateException(Error.CANT_FIND_PRODUCT.errorMessage)
}

fun findGeneralProduct(products : List<Product>, productName: String): Product? {
    return products.find { it.getName() == productName && it.getPromotion() == null }
}