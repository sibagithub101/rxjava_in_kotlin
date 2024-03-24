package com.siba.rxjavademo.solid

/**
 *  Open/Closed Principle (OCP):
 *  Software entities (classes, modules) should be open for extension, but closed for modification.
 *  This means you can add new functionalities without modifying existing code.
 *  For example, using interfaces and abstract classes allows extension by implementing new subclasses,
 *  while keeping the core behavior unaltered.
 */
//=================================================================================================
/**
 * without using Open-Close principle

class VehicleInsurance{
fun isLoyalCustomer():Boolean{
return true
}
}
class HomeInsurance{
fun isLoyalCustomer():Boolean{
return true
}
}
class LifeInsurance{
fun isLoyalCustomer():Boolean{
return true
}
}

class InsuranceCompany{
fun discountRate(vehicleInsurance: VehicleInsurance):Int{
return if(vehicleInsurance.isLoyalCustomer()) 10 else 7
}
fun discountRate(homeInsurance: HomeInsurance):Int{
return if(homeInsurance.isLoyalCustomer()) 20 else 10
}fun discountRate(lifeInsurance: LifeInsurance):Int{
return if(lifeInsurance.isLoyalCustomer()) 35 else 15
}
}**/

//==========================================================================================================

/**
 * using Open-close principle below interface is open for extension
 */

interface Customer {
    fun isLoyalCustomer(): Boolean
    fun getDiscountRate():Int

}

class VehicleInsurance : Customer {
    override fun isLoyalCustomer()=true
    override fun getDiscountRate(): Int {
        return if(isLoyalCustomer()) 5 else 9
    }


}

class HomeInsurance : Customer {
    override fun isLoyalCustomer()=true
    override fun getDiscountRate(): Int {
        return if(isLoyalCustomer()) 20 else 10
    }
}

class LifeInsurance : Customer {
    override fun isLoyalCustomer()=true
    override fun getDiscountRate(): Int {
        return if(isLoyalCustomer()) 30 else 15
    }
}

/**
 * closed for modification.
 */
class InsuranceCompany {
    fun discountRate(customer: Customer): Int {
        return (customer.getDiscountRate())
    }
}


fun main() {
    val vehicleInsurance = VehicleInsurance()
    val homeInsurance = HomeInsurance()
    val lifeInsurance = LifeInsurance()

    val insuranceCompany = InsuranceCompany()
    insuranceCompany.discountRate(homeInsurance)
    insuranceCompany.discountRate(lifeInsurance)
    insuranceCompany.discountRate(vehicleInsurance)

}