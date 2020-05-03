package by.ciszkin.basicapp.model.enums

import by.ciszkin.basicapp.R

enum class JobSurface(val title: String, val icon: Int) {
    FLOOR("Floor", R.drawable.ic_floor_icon),
    WALLS("Walls", R.drawable.ic_walls_icon),
    CEILING("Ceiling", R.drawable.ic_ceiling_icon),
    OPENING("Openings", R.drawable.ic_openings_icon),
    ALL("All surfaces", R.drawable.ic_anywhere_icon);

    override fun toString(): String {
        return title
    }

    companion object{
        fun getInstanceByTitle(title: String): JobSurface? {
            return when (title) {
                FLOOR.title -> FLOOR
                WALLS.title -> WALLS
                CEILING.title -> CEILING
                OPENING.title -> OPENING
                ALL.title -> ALL
                else -> null
            }
        }
    }
}