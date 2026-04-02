package controlFlow

class ConditionalFlow {

    fun conditionalWithResult(yearlyIncome: Double, heritage: Double): Double {
        val tax = if (yearlyIncome > heritage / 2) {
            val incomeRatio = yearlyIncome/heritage
            0.5 * incomeRatio
        } else {
            0.1
        }

        return tax;
    }

    // Using statements to execute logic
    fun taxCategoryByStatement(value: String){
        when (value) {
            "Industry" -> println("Tax is 0.5")
            "Commerce" -> println("Tas is 0.2")
        }
    }

    /**
     * Using expression to return a value
     * else is required when using expressions
     */
    fun taxCategoryByExpression(value: String){
        val taxCategory = when (value) {
            "Industry" -> 0.5
            "Commerce" -> 0.2
            else -> 0.1
        }
        println()
    }
}