package store.Util

import java.io.File
import java.nio.file.Paths

fun readFile(path: String): MutableList<MutableList<String>> {
    val projectAbsolutePath = Paths.get("").toAbsolutePath().toString()
    val fileAbsolutePath = "${projectAbsolutePath}/src/main/resources/${path}"

    val result = mutableListOf<MutableList<String>>()
    File(fileAbsolutePath).forEachLine {
        result.add(it.split(",").toMutableList())
    }
    result.removeAt(0)

    return result
}