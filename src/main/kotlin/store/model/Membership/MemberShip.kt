package store.model.Membership

import store.model.Buy.MEMBERSHIP_DISCOUNT_RATE
import store.model.Product.BuyProduct

class MemberShip(private val buyList: List<BuyProduct>) {

    fun getMemberShipDisCount(): Int {
        val notPromotionPrice =
            buyList.filter { it.promotion == null }.map { it.quantity * it.price }.reduceOrNull { acc, it -> acc + it }

        if (notPromotionPrice == null) return 0

        val discountPrice = (notPromotionPrice * MEMBERSHIP_DISCOUNT_RATE).toInt()
        return if (discountPrice > 8000) 8000 else discountPrice
    }
}