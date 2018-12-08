import java.io.File
import kotlin.math.abs

//private val input = arrayOf("1, 1", "1, 6", "8, 3" , "3, 4" , "5, 5" , "8, 9")
private val input = File("data/pedro.txt").readLines().toTypedArray()
//4171 Pedro
//private val input = File("data/input-day6.txt").readLines().toTypedArray()

fun main(args: Array<String>) = println(calculateClosest(input))

val getPoints: (Array<String>) -> List<Point> = {coords ->  coords.mapIndexed { index, l -> val (x, y) = l.split(", "); Point(x,y,index) } }

fun calculateRegionUnder(coords: Array<String>, maxDistance: Int = 10000): String {
    val points = getPoints(coords)
    val getAreaToCalculate: (List<Point>) -> List<Point> = { original ->
        val border = (maxDistance/original.size)
        val minX:Int = original.minBy { p -> p.x }!!.x - border
        val maxX:Int = original.maxBy { p -> p.x }!!.x + border
        val minY:Int = original.minBy { p -> p.y }!!.y - border
        val maxY:Int = original.maxBy { p -> p.y }!!.y + border

        (minX..maxX).fold(emptyList(), { acc, x -> acc + (minY..maxY).fold(emptyArray<Point>(), { yArray, y -> yArray + Point(x, y, "", 0) }) })
    }

    val calculateManhattan: (Point, List<Point>) -> List<Point> = { p, rec ->
        rec.map { (x, y, id, distance) -> Point(x, y, id, distance + abs(p.x - x) + abs(p.y - y)) }.filter{p -> p.distance <  maxDistance}
    }

    return points.fold(getAreaToCalculate(points), {acc, point -> calculateManhattan(point, acc)}).count().toString()
}


fun calculateClosest(coords: Array<String>): String {
    val calculateManhattan:(Point, List<Point>) -> List<Point> = { p, rec ->
        rec.map { (x, y, id, distance) ->
            val pointDistance = abs(p.x - x) + abs(p.y - y)
            when {
                distance > pointDistance -> Point(x, y, p.id, pointDistance)
                distance == pointDistance -> Point(x, y, ".", pointDistance)
                else -> Point(x, y, id, distance)
            }
        }
    }

    val points = getPoints(coords)
    val minX:Int = points.minBy { p -> p.x }!!.x
    val maxX:Int = points.maxBy { p -> p.x }!!.x
    val minY:Int = points.minBy { p -> p.y }!!.y
    val maxY:Int = points.maxBy { p -> p.y }!!.y

    val getAreaToCalculate:List<Point> = (minX..maxX).fold(emptyList(), { acc, x -> acc + (minY..maxY).fold(emptyList<Point>(), { yArray, y -> yArray + Point(x, y) }) })
    val outerAreas: List<Point> = (minY..maxY).fold(emptyList<Point>(), { yArray, y -> yArray + Point(minX, y) }) +
                    (minY..maxY).fold(emptyList<Point>(), { yArray, y -> yArray + Point(maxX, y) }) +
                    (minX..maxX).fold(emptyList<Point>(), { xArray, x -> xArray + Point(x, minY) }) +
                    (minX..maxX).fold(emptyList<Point>(), { xArray, x -> xArray + Point(x, maxY) })

    val manhattanDistances = points.fold(getAreaToCalculate, {acc, point -> calculateManhattan(point, acc)})
    val infiniteAreasIds = points.fold(outerAreas, {acc, point -> calculateManhattan(point, acc)})
            .fold(emptySet<String>(), { acc, point -> acc + point.id})


    val a = manhattanDistances
            .filterNot { p-> p.id in infiniteAreasIds}
            .filterNot { p->p.id === "."}
            .groupingBy { p -> p.id }.eachCount()!!.maxBy { (_, v) -> v }!!.value
    return a.toString()
}


data class Point ( val x: Int, val y: Int, val id: String = "", val distance: Int = Int.MAX_VALUE){
    constructor(x: String, y: String, i: Int) : this(x.toInt(), y.toInt(), i.toString())
}