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
    var modeDefault = true

    //default false：default not use easy mode
    @JvmField
    var modeEasy = false

    //module name suffix
    @JvmField
    @OptionTag(converter = ModuleNameSuffixConverter::class)
    var module = ModuleNameSuffix(
        viewName = "Page", viewFileName = "View", logicName = "Logic",
        stateName = "State",
    )

    //select function
    @JvmField
    @OptionTag(converter = FunctionInfoConverter::class)
    var function = FunctionInfo(
        useFolder = true, usePrefix = false, isPageView = false,
        addBinding = false, addLifecycle = false, autoDispose = false,
        lintNorm = false, funTabIndex = 0,
    )

    //setting info
    @JvmField
    @OptionTag(converter = SettingInfoConverter::class)
    var setting = SettingInfo(
        lint = false, flutterLints = true, lintNormIndex = 1,
        useFolderSuffix = false,
    )

    ///default true
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templatePage = TemplateInfo(view = "Page", selected = true)

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateComponent = TemplateInfo(view = "Component", selected = false)

    ///default false
    @JvmField
    @OptionTag(converter = TemplateInfoConverter::class)
    var templateCustom = TemplateInfo(view = "Widget", selected = false)


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