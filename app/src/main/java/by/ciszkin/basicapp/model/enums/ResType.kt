package by.ciszkin.basicapp.model.enums

import by.ciszkin.basicapp.R

enum class ResType(val title: String, val icon: Int) {
    MATERIAL("Материал", R.drawable.ic_materials_icon),
    TOOL("Механизм", R.drawable.ic_tools_icon);

    override fun toString(): String {
        return title
    }

    companion object{
        fun getInstanceByTitle(title: String): ResType? {
            return when (title) {
                MATERIAL.title -> MATERIAL
                TOOL.title -> TOOL
                else -> null
            }
        }
    }
}