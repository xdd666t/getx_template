package setting

import com.intellij.openapi.options.Configurable
import helper.DataService.Companion.instance
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SettingsConfigurable : Configurable {
    private val data = instance
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
        return (mSetting!!.logicName.text != data.logicName
                || mSetting!!.stateName.text != data.stateName
                || mSetting!!.viewName.text != data.viewName
                || mSetting!!.viewFileName.text != data.viewFileName)
    }

    override fun apply() {
        data.logicName = mSetting!!.logicName.text
        data.stateName = mSetting!!.stateName.text
        data.viewName = mSetting!!.viewName.text
        data.viewFileName = mSetting!!.viewFileName.text
    }

    override fun reset() {
        mSetting!!.logicName.text = data.logicName
        mSetting!!.stateName.text = data.stateName
        mSetting!!.viewName.text = data.viewName
        mSetting!!.viewFileName.text = data.viewFileName
    }

    override fun disposeUIResources() {
        mSetting = null
    }
}