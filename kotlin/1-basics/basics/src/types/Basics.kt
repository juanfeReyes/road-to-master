package types

class Basics {

    fun variables() {
        val immutable = "ABC";
        // val creates const variables that are immutable
        // immutable = "Not valid";

        var mutable = "XTZ";
        mutable = "Valid update";

        // Valid initialization after declaration for const
        val count: Int;
        count = 2;
    }

    fun typeCheck(obj: Any): Boolean? {
        if (obj is String) {
            // obj is type casted to string
            return obj.contains("A")
        }

        // obj remains as Any
        return null
    }
}