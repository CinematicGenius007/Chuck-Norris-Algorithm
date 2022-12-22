package chucknorris

import java.lang.StringBuilder

fun encode(string: String): String {
    var encoding = ""
    for (i in string) {
        encoding += "%07d".format(Integer.toBinaryString(i.code).toInt())
    }

    var type: Char? = null
    var currentCount = 0
    val encodedString = StringBuilder()

    for (i in encoding) {
        if (type == null) {
            type = i
            currentCount++
        } else if (type != i) {
            encodedString.append("${if (type == '0') "00" else "0"} ${"0".repeat(currentCount)} ")
            type = i
            currentCount = 1
        } else currentCount++
    }
    encodedString.append("${if (type == '0') "00" else "0"} ${"0".repeat(currentCount)}")  // the last sequence
    return encodedString.toString()
}

fun validateEncodeString(string: String): String {
    if (!"^[\\s0]+$".toRegex().matches(string)) throw Exception()
    if (string.split(" ").size % 2 != 0) throw Exception()
    val list = string.split(" ")
    if ((0..list.lastIndex step 2).any { i -> list[i].isEmpty() || list[i].length > 2 }) throw Exception()
    return string
}

fun decode(string: String): String {
    val list = validateEncodeString(string.trim()).split(" ")
    val binaryString = StringBuilder()
    val type = { i: String -> if (i == "0") "1" else "0" }
    for (i in list.indices step 2) {
        binaryString.append(type(list[i]).repeat(list[i + 1].length))
    }
    if (binaryString.length % 7 != 0) throw Exception()
    println("Decoded string:")
    return binaryString.chunked(7).map { Integer.parseInt(it, 2).toChar() }.joinToString("")
}

fun main() {
    while (true) {
        println("Please input operation (encode/decode/exit):")
        when (val command = readln().trim()) {
            "encode" -> {
                println("Input string:")
                val string = readln()
                println("Encoded string:")
                println(encode(string))
            }
            "decode" -> {
                println("Input encoded string:")
                try {
                    println(decode(readln()))
                } catch (e: Exception) {
                    println("Encoded string is not valid.")
                }
            }
            "exit" -> break
            else -> println("There is no '$command' operation")
        }
        println()
    }
    println("Bye!")
}