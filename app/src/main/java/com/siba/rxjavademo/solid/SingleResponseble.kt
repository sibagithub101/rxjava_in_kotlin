package com.siba.rxjavademo.solid

/**
 *  Single Responsibility Principle (SRP):
 *  A class should have one, and only one, reason to change. This means each class should be focused
 *  on a single purpose and avoid handling unrelated functionalities.
 *  This simplifies the code and makes it easier to understand and modify.
 */
class Square{
    fun calculatorArea(side:Int):Int{
        return side*side
    }

    fun calculatePerimeter(side: Int):Int{
        return 4*side
    }
}
class PrintUi{
    fun printSqure(){
        println("Print Square")
    }
    fun printRetangle(){
        println("Print Rectangle")
    }
}
fun main() {
   val square = Square()
    println(square.calculatorArea(5))
    println(square.calculatePerimeter(10))

    val printUi = PrintUi()
    printUi.printRetangle()
    printUi.printSqure()
}
