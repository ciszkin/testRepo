package by.ciszkin.basicapp.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

//class Converters {
//    @TypeConverter
//    fun storedStringToMyObjects(data: String?): List<ResourceConsumption>? {
//        val gson = Gson()
//        if (data == null) {
//            return null
//        }
//        val listType: Type = object : TypeToken<List<ResourceConsumption>?>() {}.type
//        return gson.fromJson<List<ResourceConsumption>>(data, listType)
//    }
//
//    @TypeConverter
//    fun myObjectsToStoredString(myObjects: List<ResourceConsumption?>?): String? {
//        val gson = Gson()
//        return gson.toJson(myObjects)
//    }

//}