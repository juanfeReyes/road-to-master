package classes

class CompanionObject {

    fun execute() {
        val anonymous = Person.createAnonymous()
    }

    class Person(val name: String) {

        /**
         * Can be user to create Builders
         */
        companion object {
            fun createAnonymous() = Person("Anonymous")
        }
    }

}