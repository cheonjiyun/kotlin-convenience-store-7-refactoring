package store.View

import store.model.Product.BuyProduct
import store.model.Product.Product
import java.text.DecimalFormat

class OutputView {
    val dec = DecimalFormat("#,###")

    fun printProducts(products: List<Product>) {
        products.forEach {
            val price = if (it.getQuantity() == 0) EMPTY else "${it.getQuantity()}개"
            println("- ${it.getName()} ${dec.format(it.getPrice())}원 $price ${it.getPromotion()?.getName() ?: ""}")
        }
    }

    fun printReceipt(
        buyList: List<BuyProduct>,
        promotionList: MutableList<BuyProduct>,
        totalPrice: Int,
        promotionDiscount: Int,
        membershipDiscount: Int
    ) {
        println(CONVENIENCE_STORE)
        println("상품명\t\t수량\t금액")
        buyList.forEach {
            println("${it.name}\t\t${it.quantity} \t${dec.format(it.price * it.quantity)}")
        }
        println(PROMOTION_LINE)
        promotionList.forEach {
            println("${it.name}\t\t${it.quantity}")
        }
        println(LINE)
        val totalQuantity = buyList.map { it.quantity }.reduce { acc, i -> acc + i }
        println("총구매액\t\t$totalQuantity\t${dec.format(totalPrice)}")
        println("행사할인\t\t\t-${dec.format(promotionDiscount)}")
        println("멤버십할인\t\t\t-${dec.format(membershipDiscount)}")
        println("내실돈\t\t\t ${dec.format(totalPrice - promotionDiscount - membershipDiscount)}")
        println()
    }


    companion object {
        private const val CONVENIENCE_STORE = "==============W 편의점================"
        private const val PROMOTION_LINE = "=============증\t정==============="
        private const val LINE = "===================================="
        private const val EMPTY = "재고 없음"
    }

}