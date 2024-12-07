package store.Promotion

import java.text.SimpleDateFormat
import java.util.Date

class Promotion(
    private val name: String,
    private val buy: Int,
    private val get: Int,
    private val start_date: String,
    private val end_date: String
) {

    fun getName(): String {
        return name
    }

    fun getBuy(): Int {
        return buy
    }

    fun getGet(): Int {
        return get
    }

    fun getStartDate(): String {
        return start_date
    }

    fun getEndDate(): String {
        return end_date
    }

    fun canApplyPromotion(): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse(start_date)
        val endDate = dateFormat.parse(end_date)
        val today = Date()

        return startDate < today && today < endDate
    }

    fun howGetQuantity(quantity: Int): Int {
        return (quantity / buy) * get
    }

}