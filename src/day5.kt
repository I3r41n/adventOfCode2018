import java.io.File

//private val input = "dabAcCaCBAcCcaDA"
private val input = File("data/input-day5.txt").readLines().first()

fun main(args: Array<String>) = println(getShortestWithoutAType())

fun cleanPolymer(s: String): String {
    val isOppositePolarity: (Char, Char) -> Boolean = {
        c1, c2 -> c1.toInt() == c2.toInt() + 32 || c2.toInt() == c1.toInt() + 32
    }

    val eliminateOppositePolarities: (String) -> String = { s ->
        s.fold("") { acc, cur -> if(acc.isNotEmpty() && isOppositePolarity(acc.last(), cur)) acc.dropLast(1) else acc + cur }
    }

    tailrec fun aux(s: String): String {
        val cleaned = eliminateOppositePolarities(s)
        return if(s == cleaned) cleaned else aux(cleaned)
    }

    return "${aux(s).length}"
}

fun getShortestWithoutAType(): String =
        'a'.rangeTo('z').toList().map { type -> cleanPolymer(input.filterNot { c -> c == type || c == (type - 32) }).toInt() }.min().toString()


