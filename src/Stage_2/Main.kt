package Stage_2

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
fun main(args:Array<String>) {
    val inpFile = File(args[1].substringBefore("-out").removePrefix("-in"))
    val myImage: BufferedImage = ImageIO.read(inpFile)
    for (x in 0 until myImage.width) {
        for (y in 0 until myImage.height) {
            val color = Color(myImage.getRGB(x, y))
            val r = color.red
            val g = color.green
            val b = color.blue
            val colorNew = Color(255-r, 255-g, 255-b)
            myImage.setRGB(x, y, colorNew.rgb)
        }
    }
    val outputFilePng = File(args[3].substringAfter("-out"))
    saveImage(myImage,outputFilePng)
}
fun saveImage(image: BufferedImage, imageFile: File) {
    ImageIO.write(image, "png", imageFile)
}