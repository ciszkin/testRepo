package by.ciszkin.basicapp.model.enums

enum class Units(val title: String) {
    KG(" кг"),
    L(" л"),
    M(" м"),
    M2(" м2"),
    PIECE(" шт");

    override fun toString(): String {
        return title
    }

    companion object{
        fun getInstanceByTitle(title: String): Units? {
            return when (title) {
                KG.title -> KG
                L.title -> L
                M.title -> M
                M2.title -> M2
                PIECE.title -> PIECE
                else -> null
            }
        }
    }
}