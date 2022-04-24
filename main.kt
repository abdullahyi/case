import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun main(args: Array<String>) {
    val inputStream: InputStream = File("/Users/abdullah.yildiz/Downloads/test.in").inputStream()
    val lineList = mutableListOf<String>()
    var totalTestCases = 0
    val outputList: MutableList<String> = mutableListOf()

    inputStream.bufferedReader().forEachLine {
        if (totalTestCases == 0)
            totalTestCases = it.toInt()
        else
            lineList.add(it)
    }

    var lastWordIndex = 1
    var lengthOfDictionaryIndex = 0
    var dictionary = true
    val map = mutableMapOf<String, MutableSet<String>>()
    while (lineList.size >= lastWordIndex) {
        val nextIndex = lastWordIndex.plus(lineList[lengthOfDictionaryIndex].toInt())

        for (i in lastWordIndex until nextIndex) {
            val lineArray = lineList[i].split(" ")
            if (dictionary) {
                if (map[lineArray[0].lowercase()].isNullOrEmpty())
                    map[lineArray[0].lowercase()] = mutableSetOf(lineArray[0].lowercase())

                map[lineArray[0].lowercase()]!!.add(lineArray[1].lowercase())
                if (map[lineArray[1].lowercase()].isNullOrEmpty())
                    map[lineArray[1].lowercase()] = mutableSetOf(lineArray[1].lowercase())

                map[lineArray[1].lowercase()]!!.add(lineArray[0].lowercase())
            } else {
                var found = false
                var keyList = map[lineArray[0].lowercase()]?.map { map[it]!! }?.flatten()?.toMutableSet()
                if(!keyList.isNullOrEmpty()) {
                    for (iteration in 1 until lineList[lengthOfDictionaryIndex].toInt()) {
                        val innerKeyList =
                            keyList!!.map { key -> map[key]!!.map { map[it]!! }.flatten().toMutableSet() }.flatten()
                                .toMutableSet()
                        if (innerKeyList.contains(lineArray[1].lowercase()))
                            found = true
                        if (keyList.size == innerKeyList.size)
                            break
                        keyList = innerKeyList
                    }
                }
                if(found)
                    outputList.add("synonyms")
                else
                    outputList.add("different")
            }
        }
        lastWordIndex = nextIndex + 1
        lengthOfDictionaryIndex = nextIndex
        dictionary = dictionary.not()
        if (dictionary)
            map.clear()

    }

    val outputStream: OutputStream = File("/Users/abdullah.yildiz/Downloads/test.out").outputStream()
    File("/Users/abdullah.yildiz/Downloads/test.out").printWriter().use { out ->
        outputList.forEach {
            out.println(it)
        }
    }
}
