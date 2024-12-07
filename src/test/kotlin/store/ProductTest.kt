package store

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.Product.Product

class ProductTest {
    @Test
    fun `상품을 구매하면, 구매한 수량만큼 재고가 줄어든다`() {
        val product = Product("상품이름", 2000, 10, null)
        product.buy(3)

        assert(product.getQuantity() == 10 - 3)
    }

    @Test
    fun `재고 수량보다 많은 수량을 구매하면, 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            val product = Product("상품이름", 3000, 10, null)
            product.buy(11)
        }
    }
}