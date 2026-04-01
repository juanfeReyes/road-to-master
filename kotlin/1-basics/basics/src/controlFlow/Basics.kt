package controlFlow

class Basics {

    fun conditional(a: Int, b: Int): Int {
        if (a > b){
            return a;
        } else {
            return b;
        }
    }

    fun whenExpression(obj: Any): String {
        return when (obj) {
            1 -> "one"
            "Hello" -> "Greetings"
            is Long -> "Long"
            else -> "Unknown"
        }
    }

    fun loopFor(){
        val items = listOf("Cali", "Jamundi", "Yumbo");
        for (item in items) {
            println("City: $item")
        }

        for (idx in items.indices) {
            println("City ${items[idx]}")
        }
    }


    fun loopWhile() {
        val items = listOf("Cali", "Jamundi", "Yumbo")
        var idx = 0
        while (idx < items.size) {
            println("City in while ${items[idx]}")
            idx++
        }
    }

    fun ranges(){
        for( x in 1..5){
            print("$x ")
        }
    }
}