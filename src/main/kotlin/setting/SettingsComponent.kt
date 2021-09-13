package setting

import javax.swing.JPanel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.Label
import com.intellij.ui.components.Panel
import java.awt.Container
import javax.swing.BorderFactory

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
        val margin = JPanel()
        margin.border = BorderFactory.createEmptyBorder(0, 0, 20, 0)

        val page = FormBuilder.createFormBuilder()
            .addLabeledComponent(Label("Template：Page"), JPanel())
            .addLabeledComponent(JBLabel("Logic Name: "), logicName)
            .addLabeledComponent(JBLabel("State Name: "), stateName)
            .addLabeledComponent(JBLabel("View Name: "), viewName)
            .addLabeledComponent(JBLabel("View File Name: "), viewFileName)
            .addComponentFillVertically(JPanel(), 0)
            .addLabeledComponent(Label(""), margin)
            .panel

        val component = FormBuilder.createFormBuilder()
            .addLabeledComponent(Label("Template：Component"), JPanel())
            .addLabeledComponent(JBLabel("Logic Name: "), logicName)
            .addLabeledComponent(JBLabel("State Name: "), stateName)
            .addLabeledComponent(JBLabel("View Name: "), viewName)
            .addLabeledComponent(JBLabel("View File Name: "), viewFileName)
            .addComponentFillVertically(JPanel(), 0)
            .addLabeledComponent(Label(""), margin)
            .panel

        val custom  = FormBuilder.createFormBuilder()
            .addLabeledComponent(Label("Template：Custom"), JPanel())
            .addLabeledComponent(JBLabel("Logic Name: "), logicName)
            .addLabeledComponent(JBLabel("State Name: "), stateName)
            .addLabeledComponent(JBLabel("View Name: "), viewName)
            .addLabeledComponent(JBLabel("View File Name: "), viewFileName)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        mainPanel = JPanel()
        mainPanel.add(page)
        mainPanel.add(component)
        mainPanel.add(custom)
    }
}