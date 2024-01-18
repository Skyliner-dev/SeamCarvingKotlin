package Stage_4

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import javax.imageio.ImageIO


val red = Color(255,0,0).rgb
fun withBigDecimal(value: BigDecimal, places: Int): BigDecimal {
    var bigDecimal =value
    bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP)
    return bigDecimal
}
@Synchronized
fun main(args:Array<String>) {
    val inpFile = File(args[1].substringBefore("-out").removePrefix("-in"))
    val myImage: BufferedImage = ImageIO.read(inpFile)
    val energiesList = mutableListOf(mutableListOf<BigDecimal>())
    for (x in 0 until myImage.height) {
        for (y in 0 until myImage.width) {
            val x1 = when (x) {
                0 -> 1
                myImage.height- 1 -> myImage.height - 2
                else -> x
            }
            val y1 = when (y) {
                0 -> 1
                myImage.width - 1 -> myImage.width - 2
                else -> y
            }
            val colorAlphaX = Color(myImage.getRGB(y1+1,x))
            val colorBetaX = Color(myImage.getRGB(y1-1,x))
            val colorAlphaY = Color(myImage.getRGB(y,x1+1))
            val colorBetaY = Color(myImage.getRGB(y,x1-1))
            val energy = (delta(colorAlphaX,colorBetaX)+ delta(colorAlphaY,colorBetaY)).sqrt(MathContext(100))
            energiesList[x].add(withBigDecimal(energy,20))
        }
        energiesList.add(mutableListOf())
    }
    DynamicSeam(myImage,energiesList.dropLast(1)).vSeam()
//    GreedySeam(myImage,energiesList.dropLast(1)).carving()
    saveImage(myImage, File(args[3].substringAfter("-out")))
}
class DynamicSeam(
    private val image: BufferedImage
    ,private val e: List<MutableList<BigDecimal>>
) {
    fun vSeam() {
        val eI = buildList(e.size) {
            for (row in e.indices) {
                add(e[row].map { Pair(it,false) }.toMutableList())
            }
        }.toMutableList()
        val eSum: MutableList<MutableList<Pair<BigDecimal, Boolean>>> = buildList(e.size) {
            for (row in e.indices) {
                add(e[row].map { Pair(it,false) }.toMutableList())
            }
        }.toMutableList()

        val width = e.first().size-1
        for (r in 0 until e.size-1) {
            for(c in e.first().indices) {
                e[r][c] = eSum[r][c].first
                val u = c+1
                val l = c-1
                val left = if (c!=0) e[r+1][c-1] else -1
                val middle = e[r+1][c]
                val right = if (c<width) e[r+1][c+1] else -2
                with(e[r][c]) {
                    if (left == -1) {
                        if (eSum[r+1][c].second) {
                            if (this + middle < eSum[r+1][c].first) {
                                eSum[r + 1][c] =  eSum[r+1][c].copy(first = middle+this, second = true)
                            }
                        }
                        else {
                            val y = eSum[r+1][c].first + this
                            eSum[r+1][c] = eSum[r+1][c].copy(first = y, second = true)
                        }
                        if (eSum[r+1][u].second) {
                            if (this + e[r+1][u] < eSum[r+1][u].first) {
                                val k = e[r + 1][u] + this
                                eSum[r + 1][u] = eSum[r + 1][u].copy(first = k, second = true)
                            }
                        }
                        else {
                            val o = eSum[r+1][u].first+this
                            eSum[r+1][u] = eSum[r+1][u].copy(first = o, second = true)
                        }
                    }
                    else if (right == -2) {
                        when {
                            eSum[r+1][l].second -> {
                                if (this + e[r+1][l] < eSum[r+1][l].first)
                                    eSum[r+1][l] = eSum[r+1][l].copy(first = e[r+1][l]+this, second = true)
                            }
                            else -> eSum[r+1][l] = eSum[r+1][l].copy(first = eSum[r+1][l].first+this, second = true)
                        }
                        if (eSum[r+1][c].second) {
                            if (this + e[r+1][c] < eSum[r+1][c].first)
                                eSum[r+1][c] = eSum[r+1][c].copy(first = e[r+1][c]+this, second = true)
                        }
                        else eSum[r+1][c] = eSum[r+1][c].copy(first = eSum[r+1][c].first+this, second = true)

                    } else {
                        when {
                            eSum[r+1][l].second -> {
                                if (this + e[r+1][l] < eSum[r+1][l].first)
                                    eSum[r+1][l] = eSum[r+1][l].copy(first = e[r+1][l]+this, second = true)
                            }
                            else -> eSum[r+1][l] = eSum[r+1][l].copy(first = eSum[r+1][l].first + this, second = true)
                        }
                        if (eSum[r+1][c].second) {
                            if (this + e[r+1][c] < eSum[r+1][c].first)
                                eSum[r+1][c] = eSum[r+1][c].copy(first = e[r+1][c]+this, second = true)
                        }
                        else {
                            eSum[r+1][c] = eSum[r+1][c].copy(first = eSum[r+1][c].first+this, second = true)
                        }
                        if (eSum[r+1][u].second) {
                            if (this + e[r+1][u] < eSum[r+1][u].first)
                                eSum[r+1][u] = eSum[r+1][u].copy(first = e[r+1][u]+this, second = true)
                        }
                        else eSum[r+1][u] = eSum[r+1][u].copy(first = eSum[r+1][u].first+this, second = true)
                    }
                }
            }
        }
        val min = eSum.last().minOf { it.first }

        var lastIndex = eSum.last().indexOf(Pair(min,true))

        val codeRed = mutableListOf<Pair<Int, Int>>()

        for (i in eSum.indices.reversed()) {
            loop@for (j in eSum.first().indices) {
                if (j == lastIndex) {
                    val originalValue = eI[i][j].first
                    val sumValue = eSum[i][j].first
                    val ds = sumValue - originalValue
                    val topLeft = if (i != 0 && j!=0) eSum[i - 1][j - 1] else Pair(-1,-1)
                    val topRight = if (j < width && i!=0) eSum[i - 1][j + 1] else Pair(-1,-1)
                    when (ds) {
                        topLeft.first -> lastIndex--
                        topRight.first -> lastIndex++
                    }
                    codeRed.add(Pair(i,j))
                    break@loop

                }

            }
        }

        codeRed.forEach {
            image.setRGB(it.second,it.first, red)
        }

    }
}
fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}
//fun Int.pow(n:Int):Int {
//    var res = 1
//    for (i in 0 until n) {
//        res *= this
//    }
//    return res
//}
fun delta(c1:Color,c2:Color) = listOf(c1.red - c2.red,c1.green - c2.green,c1.blue - c2.blue)
    .sumOf { it.toBigDecimal().pow(2) }
//class GreedySeam(private val image: BufferedImage, private val e:List<MutableList<Double>>) {
//    private var turnRedPoint = 0
//    fun carving() {
//        var min:Double
//        val map = mutableMapOf<Double, Int>()
//        min = e[0][0]
//        for (y in 0 until e.first().size) {
//            map[e[0][y]] = y
//            if (min > e[0][y]) min = e[0][y]
//        }
//        image.setRGB(map[min]!!,0, red)
//        turnRedPoint = map[min]!!
//        for (x in e.indices) {
//            loop@for (y in e.first().indices) {
//                if (y == turnRedPoint && x<image.height-1) {
//                    when (y) {
//                        0 -> {
//                            val checkList = listOf(e[x+1][0], e[x +1][1])
//                            checkList
//                                .mapIndexed { index, d -> index to d }
//                                .minByOrNull { it.second }!!
//                                .first.apply {
//                                    when (this) {
//                                        0 ->
//                                            turnRedPoint = 0
//
//                                        1 ->
//                                            turnRedPoint = 1
//
//                                    }
//                                }
//                        }
//
//                        image.width-1 -> {
//
//                            val checkList = listOf(e[x+1 ][y - 1], e[x+1][y])
//                            checkList
//                                .mapIndexed { index, d -> index to d }
//                                .minByOrNull { it.second }!!
//                                .first.apply {
//                                    when (this) {
//                                        0 -> turnRedPoint = y - 1
//
//                                        1 -> turnRedPoint = y
//
//                                    }
//                                }
//
//                        }
//
//                        else -> {
//                            val checkList = listOf(e[x+1 ][y - 1], e[x+1][y], e[x+1][y + 1])
//                            checkList
//                                .mapIndexed { index, i -> index to i }
//                                .minByOrNull { it.second }!!
//                                .first.apply {
//                                    when (this) {
//                                        0 -> turnRedPoint = y - 1
//
//                                        1 -> turnRedPoint = y
//
//                                        2 -> turnRedPoint = y + 1
//
//                                    }
//                                }
//
//                        }
//                    }
//                    image.setRGB(turnRedPoint,x+1,red)
//                    break@loop
//                }
//            }
//        }
//    }
//}