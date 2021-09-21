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
        mSetting = SettingsComponent()
        return mSetting!!.mainPanel
    }

    override fun isModified(): Boolean {
        return (//Page
                mSetting!!.page.logic.text != data.templatePage.logic
                        || mSetting!!.page.state.text != data.templatePage.state
                        || mSetting!!.page.view.text != data.templatePage.view
                        || mSetting!!.page.viewFile.text != data.templatePage.viewFile
                        //Component
                        || mSetting!!.component.logic.text != data.templateComponent.logic
                        || mSetting!!.component.state.text != data.templateComponent.state
                        || mSetting!!.component.view.text != data.templateComponent.view
                        || mSetting!!.component.viewFile.text != data.templateComponent.viewFile
                        //Custom
                        || mSetting!!.custom.logic.text != data.templateCustom.logic
                        || mSetting!!.custom.state.text != data.templateCustom.state
                        || mSetting!!.custom.view.text != data.templateCustom.view
                        || mSetting!!.custom.viewFile.text != data.templateCustom.viewFile
                )
    }

    override fun apply() {
        //Page
        data.templatePage.logic = mSetting!!.page.logic.text
        data.templatePage.state = mSetting!!.page.state.text
        data.templatePage.view = mSetting!!.page.view.text
        data.templatePage.viewFile = mSetting!!.page.viewFile.text
        //Component
        data.templateComponent.logic = mSetting!!.component.logic.text
        data.templateComponent.state = mSetting!!.component.state.text
        data.templateComponent.view = mSetting!!.component.view.text
        data.templateComponent.viewFile = mSetting!!.component.viewFile.text
        //Custom
        data.templateCustom.logic = mSetting!!.custom.logic.text
        data.templateCustom.state = mSetting!!.custom.state.text
        data.templateCustom.view = mSetting!!.custom.view.text
        data.templateCustom.viewFile = mSetting!!.custom.viewFile.text
    }

    override fun reset() {
        //page
        mSetting!!.page.logic.text = data.templatePage.logic
        mSetting!!.page.state.text = data.templatePage.state
        mSetting!!.page.view.text = data.templatePage.view
        mSetting!!.page.viewFile.text = data.templatePage.viewFile
        //component
        mSetting!!.component.logic.text = data.templateComponent.logic
        mSetting!!.component.state.text = data.templateComponent.state
        mSetting!!.component.view.text = data.templateComponent.view
        mSetting!!.component.viewFile.text = data.templateComponent.viewFile
        //custom
        mSetting!!.custom.logic.text = data.templateCustom.logic
        mSetting!!.custom.state.text = data.templateCustom.state
        mSetting!!.custom.view.text = data.templateCustom.view
        mSetting!!.custom.viewFile.text = data.templateCustom.viewFile
    }

    override fun disposeUIResources() {
        mSetting = null
    }
}