import java.io.File

//val input: Array<String> = arrayOf("[1518-11-01 00:00] Guard #10 begins shift",
//        "[1518-11-01 00:30] falls asleep",
//        "[1518-11-01 00:25] wakes up",
//        "[1518-11-02 00:50] wakes up",
//        "[1518-11-01 00:55] wakes up",
//        "[1518-11-01 00:05] falls asleep",
//        "[1518-11-02 00:40] falls asleep",
//        "[1518-11-01 23:58] Guard #99 begins shift",
//        "[1518-11-04 00:02] Guard #99 begins shift",
//        "[1518-11-03 00:24] falls asleep",
//        "[1518-11-03 00:29] wakes up",
//        "[1518-11-03 00:05] Guard #10 begins shift",
//        "[1518-11-04 00:36] falls asleep",
//        "[1518-11-04 00:46] wakes up",
//        "[1518-11-05 00:45] falls asleep",
//        "[1518-11-05 00:55] wakes up",
//        "[1518-11-05 00:03] Guard #99 begins shift"
//).sortedArray()

private val input = File("data/input-day4.txt").readLines().sorted()
fun main(args: Array<String>) = println(calculateMostAsleepAtAMinute())

fun calculateMostMinutesAsleep(): String {
    val calculateMinutes: (List<SleepReport>) -> Int = { list -> list.fold(0, { acc, cur -> acc + cur.final - cur.initial }) }

    val mostAsleep = getGuardsReport().fold(emptyMap<String, Int>(), { acc, cur -> acc + mapOf(Pair(cur.id, acc.getOrDefault(cur.id, 0) + calculateMinutes(cur.sleepingTime))) }).maxBy { (_, v) -> v }!!.key
    val minute = mostAsleepAtMinute(mostAsleep)!!.key
    return (minute * mostAsleep.toInt()).toString()
}

fun calculateMostAsleepAtAMinute(): String {
    val sleeper = getGuardsReport().filterNot { g -> g.sleepingTime.isEmpty() }.map { g -> Pair(g.id, mostAsleepAtMinute(g.id)!!.key) }.maxBy { it.second }
    return "${sleeper!!.first.toInt() * sleeper.second}"
}

private fun mostAsleepAtMinute(id: String) = getGuardsReport().filter { g -> g.id == id }
        .map { gr -> gr.sleepingTime }.flatten().fold(emptyArray<Int>(), { acc, cur -> acc + ((cur.initial until cur.final).toList()) }).sorted()
        .groupingBy { it }.eachCount().maxBy { (_, v) -> v }


private fun getGuardsReport(): List<GuardReport> = input.fold(emptyList(), { acc, cur ->
    when {
        cur.contains("Guard") -> acc + listOf(GuardReport(cur.split("#")[1].split(" ")[0]))
        cur.contains("wakes") -> {
            val report = acc.last().sleepingTime.last()
            acc.dropLast(1) + acc.last().copy(sleepingTime = acc.last().sleepingTime.dropLast(1) + listOf(report.copy(report.initial, cur.split(":")[1].split("]")[0].toInt())))
        }
        else ->
            acc.dropLast(1) + acc.last().copy(sleepingTime = acc.last().sleepingTime + listOf(SleepReport(cur.split(":")[1].split("]")[0].toInt())))
    }
})


data class SleepReport(val initial: Int, val final: Int = 0)
data class GuardReport(val id: String, var sleepingTime: List<SleepReport> = emptyList())
