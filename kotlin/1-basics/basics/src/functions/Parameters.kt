package functions

class Parameters {

    fun execute(){
        computeWithDefaultArgs("Sari")
        computeWithDefaultArgs("Juan", 0.5, 1000.5)
        computeWithDefaultArgs(name = "Ana", tax = 0.3)

        // -----------
        computeWithCallback("juan") {  println("") }

        // -----------
        computeVariableArgs("hola", "como", "amaneces")

        // -----------
        computeWithGenerics("Duplicate")
    }

    fun computeWithDefaultArgs(name: String, tax: Double = 0.1, income: Double = 1000.0) {

    }

    fun computeWithCallback(name: String, callback: () -> Unit) {
        callback()
    }

    fun computeVariableArgs(vararg args: String) {
        for (arg in args) {
            println(arg)
        }
    }

    fun <T> computeWithGenerics(param: T): List<T>{
        val result = ArrayList<T>()
        for (d in 1..5){
            result.add(param)
        }
        return result
    }
}