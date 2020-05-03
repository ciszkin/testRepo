package by.ciszkin.basicapp.ui.fragments.newresource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.model.RawResource
import by.ciszkin.basicapp.model.enums.ResType
import by.ciszkin.basicapp.model.enums.Units

class NewResourceViewModel : ViewModel() {

    val resourceName = MutableLiveData("")
    val resourceTypeName = MutableLiveData<Int>()
    val resourceTypeIcon = MutableLiveData<Int>()
    val resourcePrice = MutableLiveData(0.0)
    val resourceUnits = MutableLiveData("")
    val unitsList = Units.values()

    private var resourceType = ResType.MATERIAL

    init {
        resourceTypeName.value = R.string.material_label
        resourceTypeIcon.value = R.drawable.ic_materials_icon
    }

    fun changeResourceType(isTool: Boolean) {
        Log.e("MyDebug", "resource: ${resourceType.title}")
        when (isTool) {
            true -> {
                resourceType = ResType.TOOL
                resourceTypeName.value = R.string.tool_label
                resourceTypeIcon.value = ResType.TOOL.icon
            }
            false -> {
                resourceType = ResType.MATERIAL
                resourceTypeName.value = R.string.material_label
                resourceTypeIcon.value = ResType.MATERIAL.icon
            }
        }
    }

    fun changeResourceTypeToTool() {
        resourceType = ResType.TOOL
        resourceTypeName.value = R.string.tool_label
        resourceTypeIcon.value = ResType.TOOL.icon
    }

    fun changeResourceTypeToMaterial() {
        resourceType = ResType.MATERIAL
        resourceTypeName.value = R.string.material_label
        resourceTypeIcon.value = ResType.MATERIAL.icon
    }

    fun createResource() {
        val objectId = "user_defined_resource_" + System.currentTimeMillis()
        RawResource.list.add(
            RawResource(
                objectId,
                resourceName.value!!,
                resourceType,
                Units.getInstanceByTitle(resourceUnits.value!!)!!,
                resourcePrice.value!!
                )
        )
    }
}