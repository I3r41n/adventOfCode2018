import java.io.File
import java.util.*

/**
 * Created by bsantos on 02/12/2018.
 */
fun main(args: Array<String>) = println(calculateSimilarIds())


fun calculatChecksum(): Int {
//    val ids: Array<String> = arrayOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
    val ids = File("data/input-day2.txt").readLines()

    val frequencies = ids.map{ id ->
        val occurrences: MutableMap<Char, Int> = hashMapOf()
        id.forEach { c -> occurrences.put(c, id.filter {i -> i == c}.length) }
        occurrences
    }

    val occursTwice = frequencies.filter{f -> f.containsValue(2)}.size
    val occursTrice = frequencies.filter{f -> f.containsValue(3)}.size
    return occursTrice * occursTwice
}

fun calculateSimilarIds(): String {
//    val ids: Array<String> = arrayOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz").sortedArray()
    val ids = File("data/input-day2.txt").readLines().sorted()

    fun findSimilar(original: String, candidates: Array<String>): Optional<String> {
        val getCommonBody: (String, String) -> String = { s1: String, s2:String -> println("$s1 $s2");s2.filterIndexed { index, c -> s1[index] ==c}}
        val calculateNumberOfDifferences: (String) -> Int = { candidate: String ->
            candidate.foldIndexed(0, { index, acc, c -> if (c == original[index]) acc + 0 else acc + 1 })
        }

        val maybeCandidates = candidates.find { s:String ->  1 == calculateNumberOfDifferences(s) }
        return if(maybeCandidates.isNullOrEmpty()) Optional.empty() else Optional.of(getCommonBody(original, maybeCandidates.toString()))
    }

    tailrec fun aux(id: String, ids: Array<String>): String {
        return if(ids.isEmpty()) "ERROR" else {
            val maybeSimilar = findSimilar(id, ids)
            if (maybeSimilar.isPresent) maybeSimilar.get()
            else aux(ids[0], ids.drop(1).toTypedArray())
        }
    }

    return aux(ids[0], ids.drop(1).toTypedArray())
}