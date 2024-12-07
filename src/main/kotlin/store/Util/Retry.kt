package store.Util

fun<T> printError(action : () -> T){
    try {
        action()
    }catch (e: IllegalArgumentException){
        println(e.message)
    }
}