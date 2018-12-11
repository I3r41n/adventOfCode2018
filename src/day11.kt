private val input = "8199"

//private val input = "18"
val MAX = 300
val SQUARE_SIDE = 3


fun main(args: Array<String>) = println(calculateBiggestSquare())

val calculatePowerLevel: (Int, Int) -> Int = { x,y -> ((((x + 10) * y + input.toInt())* (x + 10) / 100) % 10) - 5 }

val matrix = (0..MAX+1).map{ x -> (0..MAX+1).map{ y -> calculatePowerLevel(x, y) }}

private fun calculateCoordinate():String { //((235, 87), 28)

    val calculateSquarePowerLevel: (Int, Int) -> Int = {
        x,y -> (x until x + SQUARE_SIDE).fold(0, { acc, curX -> acc +
            (y until y + SQUARE_SIDE).fold(0, {innerSquare, curY -> innerSquare + matrix[curX][curY] })
    })}

    tailrec fun calculateFullSquarePowerLevel (m: List<PowerLevelCell> = emptyList(), x: Int, y: Int): List<PowerLevelCell> = when {
        x == (MAX-SQUARE_SIDE) && y == (MAX-SQUARE_SIDE) -> m
        y == (MAX-SQUARE_SIDE) -> calculateFullSquarePowerLevel(m + listOf(PowerLevelCell(x, y, calculateSquarePowerLevel(x, y))), x + 1, 1 )
        else -> calculateFullSquarePowerLevel(m + listOf(PowerLevelCell(x, y, calculateSquarePowerLevel(x, y))), x , y + 1 )
    }

    return calculateFullSquarePowerLevel(emptyList(), 1,1).maxBy { (_, _, power) -> power}.toString()
}

private fun calculateBiggestSquare() : String {
    fun calculateCoordBiggestSquare(x: Int, y: Int): PowerLevelCell {
       tailrec fun aux (max: Pair<Int, Int>, lastValue:Int, sideSize:Int = 1): Pair<Int, Int> {
           if( x + sideSize > MAX || y + sideSize > MAX) { return max }

           val squareValue = lastValue +
                   (x .. (x + sideSize - 1)).fold(0, {acc, cur -> acc + matrix[cur][y + sideSize - 1]}) +
                   (y until (y + sideSize - 1)).fold(0, {acc, cur -> acc + matrix[x + sideSize - 1][cur]})

           return aux( max = if(squareValue > max.first) Pair(squareValue, sideSize) else max,
                   lastValue = squareValue,
                   sideSize = sideSize + 1)
       }

        val max = aux(Pair(matrix[x][y], 1), matrix[x][y], 2)
        return PowerLevelCell(x, y, max.first, max.second)
   }

    val biggestSquareMatrix = (0..MAX+1).map { x -> (0..MAX+1).map { y -> calculateCoordBiggestSquare(x , y) }.toTypedArray() }.toTypedArray()

    return biggestSquareMatrix.reduce { acc, cur -> if(acc.maxBy { (_,_,p) -> p }!!.powerLevel > cur.maxBy { (_,_,p) -> p }!!.powerLevel) acc else cur  }
            .maxBy { (_,_,p) -> p }!!.toString()
}

private data class PowerLevelCell(val x:Int, val y:Int, val powerLevel: Int, val squareSide: Int = SQUARE_SIDE)