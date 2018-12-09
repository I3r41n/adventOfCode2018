import java.math.BigInteger
import java.util.*

private val regex = "([\\d]*) players; last marble is worth ([\\d]*) points".toRegex()

//private val input = "9 players; last marble is worth 25 points"
//private val input = "13 players; last marble is worth 7999 points"
private val input = "425 players; last marble is worth 70848 points"


fun main(args: Array<String>) = println(getWinningScore100())


private fun getWinningScore(): String {
    val (players, lastMarble) = regex.find(input)!!.destructured

    val play:(Int, Int, Pair<List<Int>, List<Int>>) -> Pair<List<Int>, List<Int>> = { player, marble, (scoreboard, ring) -> when(ring.size) {
        in arrayOf(0, 1, 2) -> Pair(scoreboard, listOf(marble) + ring)
        else -> if(marble % 23 != 0) Pair(scoreboard, listOf(marble) + ring.drop(2) + ring.take(2))
           else Pair(scoreboard.subList(0, player) + listOf(scoreboard[player] + marble + ring.reversed()[6]) + scoreboard.drop(player + 1),
                     ring.takeLast(6) + ring.dropLast(7))}}

    return (0..lastMarble.toInt()).foldIndexed(Pair(List(players.toInt()){0}, emptyList<Int>()),
            {playNumber, acc, marble -> play(playNumber % (players.toInt()), marble, acc)}).first.max().toString()

}


private fun getWinningScore100(): String {
    var ring = LinkedList<Int>()
    val (players, lastMarble) = regex.find(input)!!.destructured

    val play:(Int, Int, List<BigInteger>) -> List<BigInteger> = { player, marble, scoreboard -> when(ring.size) {
        in arrayOf(0, 1, 2) -> {
            ring.add(0, marble); scoreboard
        }
        else -> if (marble % 23 != 0) {
            ring.addLast(ring.pop()); ring.addLast(ring.pop())
            ring.push(marble)
            scoreboard
        } else {
            val score = scoreboard.subList(0, player) + listOf(scoreboard[player] + (marble + ring[ring.size - 7]).toBigInteger()) + scoreboard.drop(player + 1)
            ring.removeAt(ring.size - 7); (ring.size-1 downTo ring.size-6).forEach {  ring.push(ring.pollLast()) }
            score
        }
    }}

    return (0..lastMarble.toInt()*100).foldIndexed(List(players.toInt()){BigInteger.ZERO},
            {playNumber, acc, marble -> play(playNumber % (players.toInt()), marble, acc)}).max().toString()

}
