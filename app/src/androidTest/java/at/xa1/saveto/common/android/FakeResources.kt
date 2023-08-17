package at.xa1.saveto.common.android

class FakeResources : Resources {

    private val strings = mutableMapOf<Int, String>()
    fun given(id: Int, value: String) {
        strings[id] = value
    }

    override fun string(id: Int): String =
        strings[id] ?: error("ID not defined $id")

    override fun string(id: Int, vararg args: Any): String =
        strings[id] ?: error("ID not defined $id")
}
