package store.Product

import store.Promotion.Promotion

data class BuyProduct(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: Promotion?
)
