package helper

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter

// set default value
object GetXConfig {
    //default mode
    var modeDefault = ModeInfo(name = "Default", selected = true)

    //default mode
    var modeEasy = ModeInfo(name = "Easy", selected = false)

    //default true
    const val useFolder = true

    //default false
    const val usePrefix = false

    //pageView
    const val isPageView = false

    //auto dispose GetXController
    const val autoDispose = false

    //add Lifecycle
    const val addLifecycle = false

    //add binding
    const val addBinding = false

    //support lint norm
    const val lintNorm = false

    //Logical layer name
    const val logicName = "Logic"

    //view layer name
    const val viewName = "Page"
    const val viewFileName = "View"

    //state layer name
    const val stateName = "State"

    //page template name
    var templatePage = TemplateInfo(view = "Page", selected = true, name = "Page")

    //component template name
    var templateComponent = TemplateInfo(view = "Component", selected = false, name = "Component")

    //custom template name
    var templateCustom = TemplateInfo(view = "Widget", selected = false, name = "Custom")

    //function tab index
    const val funTabIndex = 0
}

class ModeInfo(
    val name: String = "Default",
    var selected: Boolean = true,
)

data class TemplateInfo(
    var logic: String = "Logic",
    var view: String = "Page",
    var viewFile: String = "View",
    var state: String = "State",
    var selected: Boolean = false,
    val name: String = "",
)

class ModeInfoConverter : Converter<ModeInfo>() {
    override fun toString(value: ModeInfo): String? {
        val gson = Gson()
        println("++++++++++++++++++++++++")
        println(value)
        return gson.toJson(value)
    }

    override fun fromString(value: String): ModeInfo? {
        val gson = Gson()
        println("------------------------")
        println(value)
        return gson.fromJson(value, ModeInfo::class.java)
    }
}

class TemplateInfoConverter : Converter<TemplateInfo>() {
    override fun toString(value: TemplateInfo): String? {
        val gson = Gson()
        println("++++++++++++++++++++++++")
        println(value)
        return gson.toJson(value)
    }

    override fun fromString(value: String): TemplateInfo? {
        val gson = Gson()
        println("------------------------")
        println(value)
        return gson.fromJson(value, TemplateInfo::class.java)
    }
}
