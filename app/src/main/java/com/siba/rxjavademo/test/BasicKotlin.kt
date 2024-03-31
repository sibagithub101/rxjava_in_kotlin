package com.siba.rxjavademo.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

/**
 * Lambda Expression
 *
fun main() {
  val add:(Int,Int)->Int = {a,b ->a+b}
  val subStract:(Int,Int)->Int = {a,b -> a-b}
    println("Addition Result"+performedOperation(+5,6,add))
    println("Substraction Result"+performedOperation(+20,10,subStract))
}
 fun performedOperation(x:Int,y:Int,operation:(Int,Int)->Int):Int{
     return operation(x,y)
 }
 **/

//=========================================================================================================
/**
 *  Data-Class in kotlin
 *
fun main() {
    val person = Person("John", 25)
    println(person) // Output: Person(name=John, age=25)
}
data class Person(val name: String, val age: Int)

*/

//================================================================================================================

/**
 * Extension Function
 *
fun main() {
    val message = "Hello Kotlin"
    val modifiedMessage = message.addExclamation() // Extension function usage
    println(modifiedMessage) // Output: Hello!
}

fun String.addExclamation(): String {
    return "$this!"
}

*/

//===================================================================================================================================

/**
 * Coroutine execution
 *
fun main() = runBlocking {
    val job = launch {
        delay(1000L)
        println("Coroutine executed")
    }

    println("Hello")  // 1st execute this step
    job.join()        //then wait for delay time
    println("World")  //after print "Coroutine executed" world is print
}

*/

//=======================================================================================================================
/**
 * Sequence
 *
fun main() {
    // Generate a list of random numbers
    val numbers = List(10) { Random.nextInt(1, 100) }
    // Filter the numbers to get only even numbers
    val evenNumbers = numbers.asSequence()
        .filter { it % 2 == 0 }.toList()
    // Print the original list and the filtered list
    println("Original List: $numbers")
    println("Even Numbers: $evenNumbers")
}*/
//============================================================================================

suspend fun fetchData(): String {
    delay(2000L)
    return "Data fetched"
}

fun main() = runBlocking {
    val result = fetchData()
    println(result)
}

