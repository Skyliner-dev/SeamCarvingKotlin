# Seam carving - Kotlin

## The Sample image used for manipulation with the seam carving algorithm:

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/bf692047-723a-4de3-921e-35e2b58bb60f)


## Stage 1

Demonstration on how to work with java bufferedImage class,read, write, createGraphics by creating a buffered image with red cross.

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/1101cdfb-cad2-498b-9ffc-932d60e7e2c9)

## Stage 2

Invertion of RGB color components without alpha channel (.TYPE_INT_RGB) by reducing it by their maximum energy value 255

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/708125c8-a82c-451b-948f-08a58711fd2f)

## Stage 3

By using dual gradient function for each individual function
Say E(x,y) denotes the energy of the pixel in their respective x and y coordinates then,
E(x,y) = sqrt(Delta(x) + Delta(y)), where Delta functions are the gradients.
Where gradients is the summation of square of all colorcodes -> R(x,y)^2 + G(x,y)^2 + B(x,y)^2

Then finally normalize the resultant energy by
intensity = (255.0 * energy / maxEnergyValue).toInt()

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/2a490ce0-22c4-4f81-9b86-c95063317622)

## Stage 4

This is where the real seam finding begins, to demonstrate vertical seam by dynamic approach (Greedy is also included but in nested iteration it runs only once)
You can see dynamic approach in pictorial representation to understand better, the same is implemented in code:

[Dynamic approach to find lowest seam](https://en.m.wikipedia.org/wiki/Seam_carving#Dynamic_programming) Lowest Seam to remove is captured and turned its color component to red Color(255,0,0)

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/9d7963fb-11f6-4c96-8b3a-a67194119835)

## Stage 5

Transposing the image and using the same code can give the horizontal seam:

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/690700dd-08ea-47f4-b33c-7df23a2c6df9)

## Stage 6

Finally recurring removal of seams by the continous resultant image by desired reduction in width and height mention in CLI produces the carved image we needed
> #### Note
>
> - Please note that iterating with resultant images can be an expensive operation
> - Alternatively can track the rgb component of the initial image till the end and reduce it with list using Pair<>,Triple<> or custom data class.
 
>> args[1] represent the input image
>> args[3] represent the output image
>> args[5] represents the width to reduce
>> args[7] represents the height to reduce
>> Reduced by 125 w 50 h

![image](https://github.com/Skyliner-dev/SeamCarvingKotlin/assets/109461607/c7ac3375-aef6-4d96-9261-8e44ad013b50)
