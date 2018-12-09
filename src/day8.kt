import java.io.File

private val numberPattern = "([\\d]+)".toRegex()
private val input = File("data/input-day8.txt").readLines()[0]

fun main(args: Array<String>) = println(calculateRootValue())

private fun calculateMetadataSum ():String {
    fun getAscentry(ascentry: List<Int>): List<Int>{
        val maybeZero = ascentry.indexOf(0)
        return if (maybeZero == -1) ascentry else ascentry.subList(0, maybeZero + 1)
    }

    tailrec fun sum(data: List<Int>, aux: List<Int> = emptyList()): Int = when {
        data.isEmpty() -> 0
        data.first() == 0 -> {
            val toConsumeFromFamily = getAscentry(aux)
            val toConsume = data[1] + toConsumeFromFamily.sum()
            sum(data.drop(toConsume + 2), aux.drop(toConsumeFromFamily.size)) + (2 until 2 + toConsume).fold(0, { acc, i -> acc + data[i] })
        }
        else -> sum(data.drop(2), List(data.first()-1){0} + listOf(data[1]) + aux)
    }

    val data = numberPattern.findAll(input).asSequence().map { m -> m.value.toInt() }
    return sum(data.toList()).toString()
}

private fun calculateRootValue(): String {
    tailrec fun calculate (data: List<Int>, children: List<List<Int>> = emptyList(), references: List<Int> = emptyList()): Int = when {
        children.isNotEmpty() && !children.first().contains(-1) -> {
            val values = children.first()
            val value = data.subList(0, references.first()).fold(0, {acc, cur -> acc + if(cur > values.size) 0 else values[cur -1]})
            if(children.size > 1) {
                val index = children[1].indexOf(-1)
                val first = children[1].mapIndexed { i, c -> if(i == index) value else c }
                calculate(data.drop(references.first()), listOf(first) + children.drop(2), references.drop(1))
            } else value
        }
        data[0] == 0 -> {
            val index = children.first().indexOf(-1)
            val first = children.first().mapIndexed { i, c -> if(i == index) data.drop(2).subList(0, data[1]).sum() else c }
            calculate(data.drop(2 + data[1]), listOf(first) + children.drop(1), references)
        }
        else -> calculate(data.drop(2), listOf(List(data[0]){-1}) + children, listOf(data[1]) + references)
    }
    val data = numberPattern.findAll(input).asSequence().map { m -> m.value.toInt() }.toList()
    return calculate(data).toString()

}



