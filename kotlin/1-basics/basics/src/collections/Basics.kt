package collections

class Basics {

    val carBrands = listOf("Toyota", "Mazda", "Audi", "BMW")

    fun containsBrand(brand: String): Boolean {
        return brand in carBrands
    }

    fun filterSortedBrands(filterBy: String): List<String> {
        return carBrands.filter { it.contains(filterBy) }
            .sortedBy { it }
    }
}