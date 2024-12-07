package store.Buy

import store.Enum.Error
import store.Product.BuyProduct
import store.Product.Product
import store.Promotion.Promotion
import store.View.InputView
import store.View.OutputView


val MEMBERSHIP_DISCOUNT_RATE = 0.3

class BuyController(private val promotions: List<Promotion>, private var products: List<Product>) {
    private val inputView = InputView()
    private val outputView = OutputView()

    private lateinit var buyList: List<BuyProduct>
    private var promotionList = mutableListOf<BuyProduct>()
    private var totalPrice = 0
    private var promotionDiscount = 0
    private var membershipDiscount = 0


    private fun findPromotion(promotionName: String): Promotion {
        return promotions.find { it.getName() == promotionName }
            ?: throw IllegalStateException(Error.CANT_FIND_PROMOTION.errorMessage)
    }

    private fun findProduct(productName: String): Product {
        return products.find { it.getName() == productName }
            ?: throw IllegalStateException(Error.CANT_FIND_PRODUCT.errorMessage)
    }

    private fun findGeneralProduct(productName: String): Product? {
        return products.find { it.getName() == productName && it.getPromotion() == null }
    }

    private fun removeNoPromotion(buyProduct: BuyProduct) {
        val product = findProduct(buyProduct.name)
        buyProduct.quantity = product.getQuantity()
    }

    private fun checkRemoveNoPromotion(buyProduct: BuyProduct, storeProduct: Product) {
        if (!inputView.isBuyNotPromotion(buyProduct.name, buyProduct.quantity - storeProduct.getQuantity())) {
//            removeNoPromotion(buyProduct)
        }
    }


    private fun addPromotion(buyProduct: BuyProduct) {
        val promotion = findPromotion(buyProduct.promotion!!.getName())
        buyProduct.quantity += promotion.getGet()

    }

    private fun isCanAddPromotion(buyProduct: BuyProduct): Boolean {
        val promotion = findPromotion(buyProduct.promotion!!.getName())
        val product = findProduct(buyProduct.name)

        if (product.getQuantity() == buyProduct.quantity) return false
        if (buyProduct.quantity == promotion.getBuy()) return true
        if (buyProduct.quantity % (promotion.getBuy() + promotion.getGet()) == promotion.getBuy()) return true

        return false
    }

    private fun checkAddPromotion(buyProduct: BuyProduct) {
        if (isCanAddPromotion(buyProduct) && inputView.canGetPromotion(buyProduct.name)) {
            // 프로모션보다 작게 가져온 경우
            addPromotion(buyProduct)
        }
    }

    private fun calculatePromotion() {
        val discountPrice = promotionList.map { it.quantity * it.price }.reduceOrNull { acc, it -> acc + it }
        if (discountPrice != null) promotionDiscount = discountPrice
    }

    private fun applyPromotion(buyProduct: BuyProduct) {
        // 몇개 증정하는지
        val promotion = promotions.find { buyProduct.promotion?.getName() == it.getName() }
        val product = findProduct(buyProduct.name)
        val canPromotionQuantity =
            if (product.getQuantity() > buyProduct.quantity) buyProduct.quantity else product.getQuantity()

        if (promotion?.canApplyPromotion() == true) {
            try {
                // 프로모션 재고는 있는데, 증정수량+구매수량 만큼은 없음
//                if (promotion.howGetQuantity(canPromotionQuantity) + buyProduct.quantity > product.getQuantity()) throw IllegalStateException(
//                    Error.EXCEED_QUANTITY.errorMessage
//                )
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
        val storeProduct = findProduct(buyProduct.name)
        val promotion = storeProduct.getPromotion()?.let { findPromotion(it.getName()) }

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

    private fun checkPromotions() {
        buyList.forEach { buyProduct ->
            checkPromotion(buyProduct)
        }
    }

    private fun calculateTotalPrice() {
        totalPrice = buyList.map { buyProduct ->
            buyProduct.price * buyProduct.quantity
        }.reduce { acc, it -> acc + it }
    }

    private fun checkMemberShip() {
        val notPromotionPrice =
            buyList.filter { it.promotion == null }.map { it.quantity * it.price }.reduceOrNull { acc, it -> acc + it }

        if (notPromotionPrice == null) return


        val discountPrice = (notPromotionPrice * MEMBERSHIP_DISCOUNT_RATE).toInt()
        membershipDiscount = if(discountPrice > 8000) 8000 else discountPrice
    }

    private fun buyProduct(buyProduct: BuyProduct) {
        val product = findProduct(buyProduct.name)
        val howCanBuyQuantity =
            if (product.getQuantity() > buyProduct.quantity) buyProduct.quantity else product.getQuantity()
        findProduct(buyProduct.name).buy(howCanBuyQuantity)

        //프로모션이 없어서 일반상품으로 나머지 결제
        if (howCanBuyQuantity != buyProduct.quantity) {
            findGeneralProduct(buyProduct.name)?.buy(buyProduct.quantity - howCanBuyQuantity)
        }
    }

    private fun buyProducts() {
        try {
            buyList.forEach { buyProduct ->
                buyProduct(buyProduct)
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    private fun inputBuyList() {
        buyList = inputView.readItem().map { buyInput ->
            val product = findProduct(buyInput.name)
            BuyProduct(buyInput.name, product.getPrice(), buyInput.quantity, product.getPromotion())
        }
    }

    fun buyStart() {
        outputView.printProducts(products)

        inputBuyList()

        checkPromotions()
        calculatePromotion()

        if (inputView.isMembership()) {
            checkMemberShip()
        }

        buyProducts()

        calculateTotalPrice()
        outputView.printReceipt(buyList, promotionList, totalPrice, promotionDiscount, membershipDiscount)
    }
}