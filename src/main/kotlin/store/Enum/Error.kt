package store.Enum

enum class Error (private val message: String) {
    CANT_FIND_PROMOTION("프로모션이 없습니다"),
    CANT_FIND_PRODUCT("상품이 없습니다"),
    EXCEED_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    NOT_INPUT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    val errorMessage: String
        get() = ERROR + message
    companion object {
        private const val ERROR = "[ERROR] "
    }
}