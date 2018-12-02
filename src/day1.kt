import java.io.File

fun main(args: Array<String>) = println(calculateRepeatedFrequency())

fun calculateFrequency() = File("data/input-day1.txt").readLines().fold(0) {acc, line -> acc + line.toInt()}

fun calculateRepeatedFrequency(): Int {
    val lines = File("data/input-day1.txt").readLines().map { it.toInt() }
    val occurrences: MutableSet<Int> = hashSetOf(0)

    var i = 0
    var sum = 0
    while( true) {
       i = if(i == lines.size ) 0 else i
       sum += lines[i++]
       if (!occurrences.add(sum)) return sum
    }
}
//
