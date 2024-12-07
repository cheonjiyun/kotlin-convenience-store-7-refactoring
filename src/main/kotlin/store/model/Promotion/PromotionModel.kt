package store.model.Promotion

import store.Enum.Error
import store.Util.findProduct
import store.Util.findPromotion
import store.View.InputView
import store.model.Product.BuyProduct
import store.model.Product.Product

class PromotionModel (private val buyList: List<BuyProduct>, private val promotionList: MutableList<BuyProduct>,private val products : List<Product>, private val promotions : List<Promotion>, private val inputView: InputView){
    private fun isCanAddPromotion(buyProduct: BuyProduct): Boolean {
        val promotion = findPromotion(promotions, buyProduct.promotion!!.getName())
        val product = findProduct(products, buyProduct.name)

        if (product.getQuantity() == buyProduct.quantity) return false
        if (buyProduct.quantity == promotion.getBuy()) return true
        if (buyProduct.quantity % (promotion.getBuy() + promotion.getGet()) == promotion.getBuy()) return true

        return false
    }

    private fun addPromotion(buyProduct: BuyProduct) {
        val promotion = findPromotion(promotions, buyProduct.promotion!!.getName())
        buyProduct.quantity += promotion.getGet()
    }

    private fun checkAddPromotion(buyProduct: BuyProduct) {
        if (isCanAddPromotion(buyProduct) && inputView.canGetPromotion(buyProduct.name)) {
            // 프로모션보다 작게 가져온 경우
            addPromotion(buyProduct)
        }
    }

    private fun removeNoPromotion(buyProduct: BuyProduct) {
        val product = findProduct(products, buyProduct.name)
        buyProduct.quantity = product.getQuantity()
    }

    private fun checkRemoveNoPromotion(buyProduct: BuyProduct, storeProduct: Product) {
        if (!inputView.isBuyNotPromotion(buyProduct.name, buyProduct.quantity - storeProduct.getQuantity())) {
            removeNoPromotion(buyProduct)
        }
    }


    private fun applyPromotion(buyProduct: BuyProduct) {
        // 몇개 증정하는지
        val promotion = promotions.find { buyProduct.promotion?.getName() == it.getName() }
        val product = findProduct(products, buyProduct.name)
        val canPromotionQuantity =
            if (product.getQuantity() > buyProduct.quantity) buyProduct.quantity else product.getQuantity()

        if (promotion?.canApplyPromotion() == true) {
            try {
                // 프로모션 재고는 있는데, 증정수량+구매수량 만큼은 없음
                if (promotion.howGetQuantity(canPromotionQuantity) + buyProduct.quantity > product.getQuantity()) throw IllegalStateException(
                    Error.EXCEED_QUANTITY.errorMessage
                )
                // 증정 적용
                promotionList.addLast(
                    BuyProduct(
                        buyProduct.name,
                        buyProduct.price,
                        promotion.howGetQuantity(canPromotionQuantity),
                        promotion
                    )
                )
            } catch (e: IllegalStateException) {
                println(e.message)
            }
        }
    }


    private fun checkPromotion(buyProduct: BuyProduct) {
        val storeProduct = findProduct(products, buyProduct.name)
        val promotion = storeProduct.getPromotion()?.let { findPromotion(promotions, it.getName()) }

        // 해당 상품이 프로모션인가
        if (promotion == null) return

        if (storeProduct.canBuyQuantity(buyProduct.quantity)) {
            // 프로모션받을 수 있는지
            checkAddPromotion(buyProduct)
        } else {
            // 못받아도 구매할건지
            checkRemoveNoPromotion(buyProduct, storeProduct)
        }

        applyPromotion(buyProduct)

    }


    fun checkPromotions() {
        buyList.forEach { buyProduct ->
            checkPromotion(buyProduct)
        }
    }
}