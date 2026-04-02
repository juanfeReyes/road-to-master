package ErrorHandling

class Basics {

    fun inputValidity(value: String): String {
        require(value.isEmpty()) { "Value must not be empty"}

        return "Processed value $value"
    }

    fun objectStateValidity(product: Product){
        checkNotNull(product) { "Product cannot be null" }
        check(product.price >= 0) { "Product price must be positive" }
    }

    fun processProduct(product: Product) {
        when (product.category) {
            "PART" -> println("Processing part")
            "PACKAGE" -> println("Processing package")
            else -> error("Undefined category ${product.category}")
        }
    }

    class Product(var price: Double, var category: String)
}