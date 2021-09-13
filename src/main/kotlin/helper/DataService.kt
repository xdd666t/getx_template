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
    var modeDefault = GetXConfig.modeDefault

    //default false：default not use easy mode
    @JvmField
    @OptionTag(converter = ModeInfoConverter::class)
    var modeEasy = GetXConfig.modeEasy

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

    //support lint norm
    @JvmField
    var lintNorm = GetXConfig.lintNorm

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

    //function tab index
    @JvmField
    var funTabIndex = GetXConfig.funTabIndex

    ///default true
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templatePage = GetXConfig.templatePage

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateComponent = GetXConfig.templateComponent

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateCustom = GetXConfig.templateCustom


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