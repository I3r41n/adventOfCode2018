import java.io.File

private val regex = "Step ([A-Z]*) must be finished before step ([A-Z]*) can begin.".toRegex()
private val input = File("data/input-day7.txt").readLines()

//private val input = arrayOf(
//        "Step C must be finished before step A can begin.",
//        "Step C must be finished before step F can begin." ,
//        "Step A must be finished before step B can begin." ,
//        "Step A must be finished before step D can begin." ,
//        "Step B must be finished before step E can begin." ,
//        "Step D must be finished before step E can begin." ,
//        "Step F must be finished before step E can begin.")

fun main(args: Array<String>) = println(calculateAssembleTime())

val steps: () -> Map<String, List<String>> = { input
        .map{s -> regex.find(s)?.groupValues!!}
        .fold(emptyMap<String, List<String>>(), { acc, cur ->  mapOf(Pair(cur[2], acc.getOrDefault(cur[2], emptyList()) + cur[1])) +
                acc.minus(cur[2]) +
                if(!acc.containsKey(cur[1])) mapOf(Pair(cur[1], emptyList())) else emptyMap()
        })}

val removeStep: (Map<String, List<String>>, String)->Map<String, List<String>> = { process, key -> process
        .asIterable()
        .fold(emptyMap(), { acc, cur ->
            if(cur.key == key) acc
            else acc + mapOf(Pair(cur.key, cur.value.filterNot { s -> s == key }))
        })}

fun getProcessOrder(): String  {
    val nextStep: (Map<String, List<String>>)->String = { process -> process.filter{ (_,v) -> v.isEmpty() }.keys.sorted().first() }

    tailrec fun calculate(process: Map<String, List<String>>):String = if(process.isEmpty()) "" else nextStep(process) + calculate(removeStep(process, nextStep(process)))
    return calculate(steps())
}

fun calculateAssembleTime(workersNumber: Int = 5, getTime: (letter: Char)->Int = { letter -> letter.toInt()- 64 + 60 } ): String {
    val nextSteps: (Map<String, List<String>>)->List<String> = { process -> process.filter{ (_,v) -> v.isEmpty() }.keys.sorted() }
    val workers = Array(workersNumber){Pair("", 0)}

    tailrec fun calculate(process: Map<String, List<String>>, workers: Array<Pair<String, Int>>, time: Int=0): String =
            if (process.isEmpty() && workers.all { w -> w.second == 1 }) time.toString()
            else {
               var availableSteps = nextSteps(process).filterNot{s -> workers.any{w -> w.first == s}}
               val newWorkers = workers.map { worker ->
                   when {
                       worker.second > 1 -> Pair(worker.first, worker.second - 1)
                       availableSteps.isNotEmpty() -> { val step = availableSteps.first(); availableSteps = availableSteps.drop(1); Pair(step, getTime(step[0]))}
                       else -> worker
                   }
               }
               calculate(process = newWorkers.filter{w -> w.second == 1}.fold(process, {acc, cur -> removeStep(acc, cur.first)}),
                       workers = newWorkers.toTypedArray(),
                       time = time + 1)
            }

    return calculate(steps(), workers)
}