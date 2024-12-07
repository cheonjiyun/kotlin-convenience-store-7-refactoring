package store.model.Buy

import store.Enum.Error
import store.Util.findGeneralProduct
import store.Util.findProduct
import store.Util.findPromotion
import store.model.Product.BuyProduct
import store.model.Product.Product
import store.model.Promotion.Promotion
import store.View.InputView
import store.View.OutputView
import store.model.Membership.MemberShip
import store.model.Promotion.PromotionModel


val MEMBERSHIP_DISCOUNT_RATE = 0.3

class BuyController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val promotions: List<Promotion>,
    private var products: List<Product>
) {

    private lateinit var buyList: List<BuyProduct>
    private var promotionList = mutableListOf<BuyProduct>()
    private var totalPrice = 0
    private var promotionDiscount = 0
    private var membershipDiscount = 0


    private fun calculatePromotion() {
        val discountPrice = promotionList.map { it.quantity * it.price }.reduceOrNull { acc, it -> acc + it }
        if (discountPrice != null) promotionDiscount = discountPrice
    }


    private fun calculateTotalPrice() {
        totalPrice = buyList.map { buyProduct ->
            buyProduct.price * buyProduct.quantity
        }.reduce { acc, it -> acc + it }
    }


    private fun buyProduct(buyProduct: BuyProduct) {
        val product = findProduct(products, buyProduct.name)
        val howCanBuyQuantity =
            if (product.getQuantity() > buyProduct.quantity) buyProduct.quantity else product.getQuantity()
        findProduct(products, buyProduct.name).buy(howCanBuyQuantity)

        //프로모션이 없어서 일반상품으로 나머지 결제
        if (howCanBuyQuantity != buyProduct.quantity) {
            findGeneralProduct(products, buyProduct.name)?.buy(buyProduct.quantity - howCanBuyQuantity)
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
            val product = findProduct(products, buyInput.name)
            BuyProduct(buyInput.name, product.getPrice(), buyInput.quantity, product.getPromotion())
        }
    }

    fun buyStart() {
        outputView.printProducts(products)

        inputBuyList()

        val promotionModel = PromotionModel(buyList, promotionList, products, promotions, inputView)
        promotionModel.checkPromotions()

        calculatePromotion()

        if (inputView.isMembership()) {
            val memberShip = MemberShip(buyList)
            membershipDiscount = memberShip.getMemberShipDisCount()
        }

        buyProducts()

        calculateTotalPrice()
        outputView.printReceipt(buyList, promotionList, totalPrice, promotionDiscount, membershipDiscount)
    }
}