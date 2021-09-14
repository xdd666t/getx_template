package setting

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.Container
import javax.swing.*


class SettingsComponent {
    @JvmField
    var mainPanel: JPanel = JPanel()

    @JvmField
    var page = PageSetting()

    @JvmField
    var component = ComponentSetting()

    @JvmField
    var custom = CustomSetting()


    init {
//        mainPanel.border =  IdeBorderFactory.createTitledBorder("Component")

        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Template-Page"), JPanel())
            .addLabeledComponent(JBLabel("LogicName: "), page.logic)
            .addLabeledComponent(JBLabel("StateName: "), page.state)
            .addLabeledComponent(JBLabel("ViewName: "), page.view)
            .addLabeledComponent(JBLabel("ViewFileName: "), page.viewFile)
            .addLabeledComponent(JBLabel(""), dealMargin(JPanel()))
            .addLabeledComponent(JBLabel("Template-Component"), JPanel())
            .addLabeledComponent(JBLabel("LogicName: "), component.logic)
            .addLabeledComponent(JBLabel("StateName: "), component.state)
            .addLabeledComponent(JBLabel("ViewName: "), component.view)
            .addLabeledComponent(JBLabel("ViewFileName: "), component.viewFile)
            .addLabeledComponent(JBLabel(""), dealMargin(JPanel()))
            .addLabeledComponent(JBLabel("Template-Custom"), JPanel())
            .addLabeledComponent(JBLabel("LogicName: "), custom.logic)
            .addLabeledComponent(JBLabel("StateName: "),custom.state)
            .addLabeledComponent(JBLabel("ViewName: "), custom.view)
            .addLabeledComponent(JBLabel("ViewFileName: "), custom.viewFile)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }


    private fun dealMargin(jPanel: JPanel): JPanel {
        jPanel.border = BorderFactory.createEmptyBorder(0, 0, 10, 0)
        return jPanel
    }

//    private fun titledPanel(title: String, body: JPanel.() -> Unit): JComponent {
//        val innerPanel = JPanel()
//        innerPanel.body()
//        return JPanel(migLayout()).apply {
//            border = IdeBorderFactory.createTitledBorder(title)
//            add(innerPanel)
//            add(JPanel(), fillX())
//        }
//    }
}

class PageSetting{
    var logic = JBTextField()

    var state = JBTextField()

    var view = JBTextField()

    var viewFile = JBTextField()
}

class ComponentSetting {
    var logic = JBTextField()

    var state = JBTextField()

    var view = JBTextField()

    var viewFile = JBTextField()
}

class CustomSetting {
    var logic = JBTextField()

    var state = JBTextField()

    var view = JBTextField()

    var viewFile = JBTextField()
}