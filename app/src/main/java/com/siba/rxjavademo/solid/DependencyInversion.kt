package com.siba.rxjavademo.solid

/**
 * Dependency Inversion Principle (DIP):
 * High-level modules should not depend on low-level modules. Both should depend on abstractions.
 * This promotes decoupling and makes the code less susceptible to changes in low-level implementations.
 * Abstraction allows you to easily substitute different implementations without affecting
 * the higher-level modules.
 */
/**
 * Without using Dependency Inversion Principle (DIP)
 * High-Level Module

class ProductCatalog{
    fun listAllProduct(){
        val sql = SQLProductRepo()
        sql.getAllProducts().forEach {
            println(it)
        }
    }
}
//Low-Level Module
class SQLProductRepo{
    fun getAllProducts():MutableList<String>{
        return mutableListOf("Apple","Banana")
    }
}
*/

interface ProductRepo{
    fun getAllProducts():MutableList<String>
}

// High-Level Module

class ProductCatalog{
    fun listAllProduct(){
        val sql:ProductRepo = MongoProductRepo()
        sql.getAllProducts().forEach {
            println(it)
        }
    }
}

//Low-Level Module
class SQLProductRepo:ProductRepo{
    override fun getAllProducts():MutableList<String>{
        return mutableListOf("Apple","Banana")
    }
}
class MongoProductRepo:ProductRepo{
    override fun getAllProducts(): MutableList<String> {
        return mutableListOf("Grapes")
    }

}

fun main() {
    val productCatalog = ProductCatalog()
    productCatalog.listAllProduct()
}