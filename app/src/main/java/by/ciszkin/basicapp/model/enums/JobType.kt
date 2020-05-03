package by.ciszkin.basicapp.model.enums

import by.ciszkin.basicapp.R

enum class JobType(val title: String, val icon: Int) {
    PAINTING("Painting", R.drawable.ic_painting_icon),
    TILING("Tiling", R.drawable.ic_tiling_icon),
    PASTING("Pasting", R.drawable.ic_pasting_icon),
    FLATTENING("Flattening", R.drawable.ic_flattening_icon),
    MOUNTING("Mounting", R.drawable.ic_mounting_icon),
    OTHER("Other", R.drawable.ic_other_icon);

    override fun toString(): String {
        return title
    }

    companion object{
        fun getInstanceByTitle(title: String): JobType? {
            return when (title) {
                PAINTING.title -> PAINTING
                TILING.title -> TILING
                PASTING.title -> PASTING
                FLATTENING.title -> FLATTENING
                MOUNTING.title -> MOUNTING
                OTHER.title -> OTHER
                else -> null
            }
        }
    }
}