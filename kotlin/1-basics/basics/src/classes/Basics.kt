package classes

class Basics {

    open class Location(lat: Double, long: Double) {
        private val a = 1;
        protected open val b = 2;
        internal val c = 3;
        val d = 4;

        open fun toCartesianCoords(): String {
            return "x,y"
        }
    }

    class City(lat: Double, long: Double): Location(lat, long) {
        // override val a = 1 - Not visible

        override val b = 5;
        
        fun buildReport(): String{
            return """
                City
                ${toCartesianCoords()}
            """.trimIndent()
        }
    }

    open class Shipment(val id: Int, val source: String, val destination: String) {

    }

    class Route(id: Int, source: String, destination: String, coords: ArrayList<Location>): Shipment(id, source, destination) {

    }


}