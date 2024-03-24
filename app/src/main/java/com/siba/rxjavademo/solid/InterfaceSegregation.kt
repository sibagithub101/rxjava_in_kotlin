package com.siba.rxjavademo.solid

/**
 * Interface Segregation Principle (ISP):
 * Clients should not be forced to depend on methods they don't use. This principle suggests breaking
 * down large interfaces into smaller, more specific ones, allowing clients to only depend on the
 * functionalities they need. This makes the code more focused and reduces unnecessary dependencies.
 */

//=====================================================================================================================
/**
 * Without using Interface Segregation Principle (ISP)
 */


/**
interface IMultiFunctional{
    fun print()
    fun printSpoonDetails()
    fun scan()
    fun scanPhoto()
    fun fax()
    fun internetFax()
}
class CanonPrinter:IMultiFunctional{
    override fun print() {
        TODO("Not yet implemented")
    }

    override fun printSpoonDetails() {
        TODO("Not yet implemented")
    }

    override fun scan() {
        TODO("Not yet implemented")
    }

    override fun scanPhoto() {
        TODO("Not yet implemented")
    }

    override fun fax() {
        TODO("Not yet implemented")
    }

    override fun internetFax() {
        TODO("Not yet implemented")
    }
 }

*/
//===================================================================================================================
/**
 *  using Interface Segregation Principle (ISP)
 */

interface IPrint{
    fun print()
    fun printSpoonDetails()
}

interface IScan{
    fun scan()
    fun scanPhoto()
}
interface IFax{
    fun fax()
    fun internetFax()
}

class CanonPrinter:IPrint,IScan{
    override fun print() {
        TODO("Not yet implemented")
    }

    override fun printSpoonDetails() {
        TODO("Not yet implemented")
    }

    override fun scan() {
        TODO("Not yet implemented")
    }

    override fun scanPhoto() {
        TODO("Not yet implemented")
    }

}
