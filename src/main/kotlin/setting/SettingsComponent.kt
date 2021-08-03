package setting

import javax.swing.JPanel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.ui.components.JBLabel

class SettingsComponent {
    @JvmField
    var mainPanel: JPanel

    @JvmField
    var logicName: JBTextField = JBTextField()

    @JvmField
    var stateName: JBTextField = JBTextField()

    @JvmField
    var viewName: JBTextField = JBTextField()

    @JvmField
    var viewFileName: JBTextField = JBTextField()

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Logic Name: "), logicName)
            .addLabeledComponent(JBLabel("State Name: "), stateName)
            .addLabeledComponent(JBLabel("View Name: "), viewName)
            .addLabeledComponent(JBLabel("View File Name: "), viewFileName)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}