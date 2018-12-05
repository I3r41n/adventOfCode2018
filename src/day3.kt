import java.io.File

/**
 * Created by bsantos on 03/12/2018.
 */
fun main(args: Array<String>) = println(calculateNonOverlappingId())

fun calculateNonOverlappingId () : String {
    val sizes = File("data/input-day3.txt").readLines().map{l -> l.split(" ")[3].split("x").map{d -> d.toInt()}}.map{d -> d[0]*d[1]}

    return  getFabric().filterValues { l -> l.size == 1 }.values.map { v -> Pair(v[0].substring(1).toInt(), v[0].substring(1).toInt()) }
            .groupingBy { (k,v) -> v }.eachCount().filter { (k,v) -> v - sizes[k -1 ] == 0 }.keys.first().toString()
}

fun calculateOverlap (): Int = getFabric().filterValues { l -> l.size > 1  }.size


fun getFabric(): Map<Pair<Int, Int>, List<String>> {
    val getAreas: (String, String) -> List<Pair<Int, Int>> = { initialCoord: String, area: String ->
        run {
            var areas: MutableList<Pair<Int, Int>> = mutableListOf()
            val areaMeasures = area.split("x")
            val coords = initialCoord.split(",")
            (0.."${areaMeasures[1]}".toInt()-1).forEach {
                x -> (0.."${areaMeasures[0]}".toInt()-1).forEach {
                y -> areas.add(Pair(x + "${coords[1].trim(':')}".toInt(), y + "${coords[0]}".toInt())) } }
            areas
        }
    }

    val claims = File("data/input-day3.txt").readLines()
//    val claims = arrayOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")
    val fabric: MutableMap<Pair<Int, Int>, List<String>> = mutableMapOf()

    claims.forEach { claim ->
        run {
            val splitClaim = claim.split(" ")
            getAreas(splitClaim[2], splitClaim[3]).forEach { area -> fabric.put(area, fabric[area].orEmpty() + splitClaim[0]) }
        }
    }

    return fabric
}


//  _________________
//0|                |
//1|   2222         |
//2|   2222         |
//3| 11xx22         |
//4| 11xx22         |
//5| 111133         |
//6| 111133         |
//7|                |
//8|                |
//9|                |
//-------------------