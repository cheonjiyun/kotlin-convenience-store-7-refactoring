package store.model.Product

import store.model.Promotion.Promotion

data class BuyProduct(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: Promotion?
)
