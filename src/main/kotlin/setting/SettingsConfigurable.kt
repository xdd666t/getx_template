package setting

import com.intellij.openapi.options.Configurable
import helper.DataService
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SettingsConfigurable : Configurable {
    private val data = DataService.instance
    private var mSetting: SettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "GetX Setting"
    }

    override fun createComponent(): JComponent {
        val setting = SettingsComponent()
        mSetting = setting
        reset()
        return setting.mainPanel
    }

    override fun isModified(): Boolean {
        val setting = mSetting ?: return false
        return setting.useFolderSuffixBox.isSelected != data.setting.useFolderSuffix ||
            setting.page.logic.text != data.templatePage.logic ||
            setting.page.state.text != data.templatePage.state ||
            setting.page.view.text != data.templatePage.view ||
            setting.page.viewFile.text != data.templatePage.viewFile ||
            setting.component.logic.text != data.templateComponent.logic ||
            setting.component.state.text != data.templateComponent.state ||
            setting.component.view.text != data.templateComponent.view ||
            setting.component.viewFile.text != data.templateComponent.viewFile ||
            setting.custom.logic.text != data.templateCustom.logic ||
            setting.custom.state.text != data.templateCustom.state ||
            setting.custom.view.text != data.templateCustom.view ||
            setting.custom.viewFile.text != data.templateCustom.viewFile
    }

    override fun apply() {
        val setting = mSetting ?: return
        data.setting.useFolderSuffix = setting.useFolderSuffixBox.isSelected
        copyToData(setting.page, data.templatePage)
        copyToData(setting.component, data.templateComponent)
        copyToData(setting.custom, data.templateCustom)
    }

    override fun reset() {
        val setting = mSetting ?: return
        setting.useFolderSuffixBox.isSelected = data.setting.useFolderSuffix
        copyFromData(data.templatePage, setting.page)
        copyFromData(data.templateComponent, setting.component)
        copyFromData(data.templateCustom, setting.custom)
    }

    override fun disposeUIResources() {
        mSetting = null
    }

    private fun copyToData(source: Setting, target: helper.TemplateInfo) {
        target.logic = source.logic.text
        target.state = source.state.text
        target.view = source.view.text
        target.viewFile = source.viewFile.text
    }

    private fun copyFromData(source: helper.TemplateInfo, target: Setting) {
        target.logic.text = source.logic
        target.state.text = source.state
        target.view.text = source.view
        target.viewFile.text = source.viewFile
    }
}
