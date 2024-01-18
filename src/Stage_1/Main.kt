package Stage_1

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    println("Enter rectangle width:")
    val rw = readln().toInt()
    println("Enter rectangle height:")
    val rh = readln().toInt()
    println("Enter output image name:")
    val op = readln()
    saveImage(rectX(rw,rh),File(op))
}
fun rectX(rw:Int,rh:Int)= BufferedImage(rw,rh,BufferedImage.TYPE_INT_RGB).apply {
    with(this.createGraphics()) {
        color = Color.RED
        drawLine(0,0,rw-1,rh-1)
        drawLine(rw-1,0,0,rh-1)
    }
}
fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}