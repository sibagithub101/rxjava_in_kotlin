package com.siba.rxjavademo.solid

/**
 *  Liskov Substitution Principle (LSP):
 *  Subtypes should be substitutable for their base types without altering the correctness of the program.
 *  This means a child class should fulfill all the contracts of its parent class and ensure consistent
 *  behavior when substituted in its place. This promotes code reusability and prevents unexpected behavior.
 */

//=========================================================================================================================
/**
 * without using Liskov Substitution Principle

open class Car{
    fun getCabinWidth():Int{
        return 40
    }
}

class RacingCar:Car(){
    fun cockpitWidth():Int{
        return 15
    }
}

fun main() {
    val list = mutableListOf(RacingCar(),Car())
    list.forEach {
        when(it){
            is RacingCar->{
                println(it.cockpitWidth())
            }
            is Car->{
                println(it.getCabinWidth())
            }
        }
    }
}*/

//=============================================================================
/**
 *  using Liskov Substitution Principle
 */

open class Vehicle{
   open fun getInteriorWidth():Int{
       return 0
   }
}

open class Car:Vehicle(){
    override fun getInteriorWidth(): Int {
        return this.getCabinWidth()
    }
    private fun getCabinWidth():Int{
        return 40
    }
}

class RacingCar:Vehicle(){
    override fun getInteriorWidth(): Int {
        return this.cockpitWidth()
    }
    private fun cockpitWidth():Int{
        return 15
    }
}
class TransPortCar:Vehicle(){
    override fun getInteriorWidth(): Int {
        return this.getTrnsportWidth()
    }
    private fun getTrnsportWidth():Int{
        return 30;
    }
}

fun main() {
    val list = mutableListOf(RacingCar(), Car(),TransPortCar())
    list.forEach {
      println(it.getInteriorWidth())
    }
}
