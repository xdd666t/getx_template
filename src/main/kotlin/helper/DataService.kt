package helper

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag

//val modeInfoConverter: KClass<out Converter<ModeInfoConverter>> = ModeInfoConverter::class;

//custom save location
@State(name = "DataService", storages = [Storage(value = "DataService.xml")])
class DataService : PersistentStateComponent<DataService> {
    //default true: use default mode
    @JvmField
    @OptionTag(converter = ModeInfoConverter::class)
    var modeDefault =  ModeInfo(name = "Default", selected = true)

    //default false：default not use easy mode
    @JvmField
    @OptionTag(converter = ModeInfoConverter::class)
    var modeEasy = ModeInfo(name = "Easy", selected = false)

    //default true
    @JvmField
    var useFolder = true

    //default false
    @JvmField
    var usePrefix = false

    //default false
    @JvmField
    var isPageView = false

    //auto dispose GetXController
    @JvmField
    var autoDispose = false

    //add Lifecycle
    @JvmField
    var addLifecycle = false

    //add binding
    @JvmField
    var addBinding = false

    //support lint norm
    @JvmField
    var lintNorm = false

    //Logical layer name
    @JvmField
    var logicName = "Logic"

    //view layer name
    @JvmField
    var viewName = "Page"

    @JvmField
    var viewFileName = "View"

    //state layer name
    @JvmField
    var stateName = "State"

    //function tab index
    @JvmField
    var funTabIndex = 0

    ///default true
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templatePage = TemplateInfo(view = "Page", selected = true, name = "Page")

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateComponent = TemplateInfo(view = "Component", selected = false, name = "Component")

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateCustom = TemplateInfo(view = "Widget", selected = false, name = "Custom")


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