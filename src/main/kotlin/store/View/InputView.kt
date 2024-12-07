package store.View

import camp.nextstep.edu.missionutils.Console
import store.Enum.Error
import store.Product.BuyInputProduct
import java.util.*

class InputView {

    private fun input(): String {
        return Console.readLine()
    }

    fun readItem(): List<BuyInputProduct> {
        println(BUY)
        val input = input().split(',')

        val regex = Regex("\\[(.*?)]")

        return input.map {
            val produceInput = regex.find(it)?.groupValues?.get(1) ?: throw IllegalArgumentException(Error.NOT_INPUT.errorMessage)
            val (name, quantity) = produceInput.split('-')
            BuyInputProduct(name, quantity.toInt())
        }
    }

    fun isMembership(): Boolean  {
        println(IS_MEMBERSHIP)
        val input = input()

        return input.uppercase(Locale.getDefault()) == "Y"
    }

    fun isReBuy(): Boolean {
        println(IS_REBUY)
        val input = input()

        return input.uppercase(Locale.getDefault()) == "Y"
    }

    fun canGetPromotion(productName: String): Boolean {
        println(String.format(IS_ADD_PROMOTION, productName))
        val input = input()
        return input.uppercase(Locale.getDefault()) == "Y"
    }

    fun isBuyNotPromotion(productName: String, quantity: Int): Boolean {
        println(String.format(IS_NOT_PROMOTION,productName,quantity ))
        val input = input()
        return input.uppercase(Locale.getDefault()) == "Y"
    }


    companion object{
        private const val BUY = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
        private const val IS_MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)"
        private const val IS_REBUY = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"
        private const val IS_ADD_PROMOTION = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
        private const val IS_NOT_PROMOTION = "현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
    }
}