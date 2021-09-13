package helper

import com.google.gson.Gson
import com.intellij.util.xmlb.Converter

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
        return Gson().toJson(value)
    }

    override fun fromString(value: String): ModeInfo? {
        return Gson().fromJson(value, ModeInfo::class.java)
    }
}

class TemplateInfoConverter : Converter<TemplateInfo>() {
    override fun toString(value: TemplateInfo): String? {
        return Gson().toJson(value)
    }

    override fun fromString(value: String): TemplateInfo? {
        return Gson().fromJson(value, TemplateInfo::class.java)
    }
}
