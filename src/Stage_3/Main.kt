package Stage_3

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.sqrt
fun main(args:Array<String>) {

    val inpFile = File(args[1].substringBefore("-out").removePrefix("-in"))
    val myImage: BufferedImage = ImageIO.read(inpFile)
    var max = 0.0
    for (x in 0 until myImage.width) {
        for (y in 0 until myImage.height) {
            val x1 = when (x) {
                0 -> 1
                myImage.width - 1 -> myImage.width - 2
                else -> x
            }
            val y1 = when (y) {
                0 -> 1
                myImage.height - 1 -> myImage.height - 2
                else -> y
            }
            val colorAlphaX = Color(myImage.getRGB(x1+1,y1))
            val colorBetaX = Color(myImage.getRGB(x1-1,y1))
            val colorAlphaY = Color(myImage.getRGB(x1,y1+1))
            val colorBetaY = Color(myImage.getRGB(x1,y1-1))
            val energy = sqrt(delta(colorAlphaX,colorBetaX) + delta(colorAlphaY,colorBetaY))
            if (energy>max) max = energy
        }
    }
    val outputFilePng = File(args[3].substringAfter("-out"))
    val outputImage = BufferedImage(myImage.width,myImage.height,BufferedImage.TYPE_INT_RGB)
    for (x in 0 until myImage.width) {
        for (y in 0 until myImage.height) {
            val x1 = when (x) {
                0 -> 1
                myImage.width - 1 -> myImage.width - 2
                else -> x
            }
            val y1 = when (y) {
                0 -> 1
                myImage.height - 1 -> myImage.height - 2
                else -> y
            }
            val colorAlphaX = Color(myImage.getRGB(x1+1,y))
            val colorBetaX = Color(myImage.getRGB(x1-1,y))
            val colorAlphaY = Color(myImage.getRGB(x,y1+1))
            val colorBetaY = Color(myImage.getRGB(x,y1-1))
            val energy = sqrt(delta(colorAlphaX,colorBetaX) + delta(colorAlphaY,colorBetaY))
            val intensity = (255.0 * energy/max).toInt()
            val color = Color(intensity,intensity,intensity)
            outputImage.setRGB(x,y,color.rgb)
            //outputImage.setRGB(x,y,intensity)
        }
    }
    saveImage(outputImage,outputFilePng)
}
fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}
fun delta(c1:Color,c2:Color) = listOf(c1.red - c2.red,c1.green - c2.green,c1.blue - c2.blue)
    .sumOf { it.toDouble().pow(2) }