package helper

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import helper.DataService
import helper.GetXConfig
import com.intellij.util.xmlb.XmlSerializerUtil

//custom save location
@State(name = "DataService", storages = [Storage(value = "DataService.xml")])
class DataService : PersistentStateComponent<DataService> {
    //default true: use high mode
    @JvmField
    var defaultMode = GetXConfig.defaultMode

    //default true
    @JvmField
    var useFolder = GetXConfig.useFolder

    //default false
    @JvmField
    var usePrefix = GetXConfig.usePrefix

    //default false
    @JvmField
    var isPageView = GetXConfig.isPageView

    //auto dispose GetXController
    @JvmField
    var autoDispose = GetXConfig.autoDispose

    //add Lifecycle
    @JvmField
    var addLifecycle = GetXConfig.addLifecycle

    //add binding
    @JvmField
    var addBinding = GetXConfig.addBinding

    //Logical layer name
    @JvmField
    var logicName = GetXConfig.logicName

    //view layer name
    @JvmField
    var viewName = GetXConfig.viewName

    @JvmField
    var viewFileName = GetXConfig.viewFileName

    //state layer name
    @JvmField
    var stateName = GetXConfig.stateName

    override fun getState(): DataService {
        return this
    }

    override fun loadState(state: DataService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        @JvmStatic
        val instance: DataService
            get() = ServiceManager.getService(DataService::class.java)
    }
}