import java.io.File
import java.lang.Math.abs
import java.math.BigInteger

/**
 * Created by bsantos on 10/12/2018.
 */
private val regex = ".*< ?(-?[\\d]+),  ?(-?[\\d]+)>.*< ?(-?[\\d]+),  ?(-?[\\d]+)>\n".toRegex()
//private val input = "position=< 9,  1> velocity=< 0,  2>\n" +
//        "position=< 7,  0> velocity=<-1,  0>\n" +
//        "position=< 3, -2> velocity=<-1,  1>\n" +
//        "position=< 6, 10> velocity=<-2, -1>\n" +
//        "position=< 2, -4> velocity=< 2,  2>\n" +
//        "position=<-6, 10> velocity=< 2, -2>\n" +
//        "position=< 1,  8> velocity=< 1, -1>\n" +
//        "position=< 1,  7> velocity=< 1,  0>\n" +
//        "position=<-3, 11> velocity=< 1, -2>\n" +
//        "position=< 7,  6> velocity=<-1, -1>\n" +
//        "position=<-2,  3> velocity=< 1,  0>\n" +
//        "position=<-4,  3> velocity=< 2,  0>\n" +
//        "position=<10, -3> velocity=<-1,  1>\n" +
//        "position=< 5, 11> velocity=< 1, -2>\n" +
//        "position=< 4,  7> velocity=< 0, -1>\n" +
//        "position=< 8, -2> velocity=< 0,  1>\n" +
//        "position=<15,  0> velocity=<-2,  0>\n" +
//        "position=< 1,  6> velocity=< 1,  0>\n" +
//        "position=< 8,  9> velocity=< 0, -1>\n" +
//        "position=< 3,  3> velocity=<-1,  1>\n" +
//        "position=< 0,  5> velocity=< 0, -1>\n" +
//        "position=<-2,  2> velocity=< 2,  0>\n" +
//        "position=< 5, -2> velocity=< 1,  2>\n" +
//        "position=< 1,  4> velocity=< 2,  1>\n" +
//        "position=<-2,  7> velocity=< 2, -2>\n" +
//        "position=< 3,  6> velocity=<-1, -1>\n" +
//        "position=< 5,  0> velocity=< 1,  0>\n" +
//        "position=<-6,  0> velocity=< 2,  0>\n" +
//        "position=< 5,  9> velocity=< 1, -2>\n" +
//        "position=<14,  7> velocity=<-2,  0>\n" +
//        "position=<-3,  6> velocity=< 2, -1>"
private val input = File("data/input-day10.txt").readText()

fun main(args: Array<String>) = getMessage()

private fun getMessage() {
    val tickTime:(List<Pos>) -> List<Pos> = { pos -> pos.map {(x, y, vx,vy) -> Pos(x+vx,y+vy, vx, vy )} }
    val seeMessage:(List<Pos>) -> Unit = { pos ->
        (0..pos.map { (x) -> x }.max()!!)
                .map{px -> (0..pos.map { (y) -> y}.max()!!)
                        .map{py -> print(if(pos.any { (x,y) -> x == py && y == px }) "#" else " ")}; println()}}
    val data = regex.findAll(input).asSequence().map { res -> Pos(res.groupValues.drop(1)) }.toList()

    tailrec fun aux(sky: List<Pos>, tick: Int = 0): List<Pos> {
        val area:(List<Pos>) -> BigInteger = { coords -> if( coords.isEmpty()) BigInteger.ZERO
            else abs(coords.map{ (_,y) -> y}.max()!!).toBigInteger() * abs(coords.map{ (x) -> x}.max()!!).toBigInteger() }
        val next = tickTime(sky)
        return if (sky.isNotEmpty() && area(next) > area(sky)) {
//            seeMessage(sky)
            println(tick)
            sky
        } else aux(next, tick + 1)
    }

    aux(data)
}

private data class Pos (val x: Int , val y: Int , val velx: Int , val vely: Int) {
    constructor(list: List<String>):this(list[0].toInt(), list[1].toInt(), list[2].toInt(), list[3].toInt())
}