// MODE: function_return
val o = object : Iterable<Int> {
        override fun iterator()<# [:  [jar://kotlin-stdlib-sources.jar!/kotlin/Iterator.kt:*]Iterator < [jar://kotlin-stdlib-sources.jar!/kotlin/Primitives.kt:*]Int >] #> = object : Iterator<Int> {
        override fun next()<# [:  [jar://kotlin-stdlib-sources.jar!/kotlin/Primitives.kt:*]Int] #> = 1
        override fun hasNext()<# [:  [jar://kotlin-stdlib-sources.jar!/kotlin/Boolean.kt:*]Boolean] #> = true
    }
}
