package setting

import com.intellij.refactoring.suggested.main
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.AWTEventMulticaster.add
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.Toolkit
import javax.naming.CompositeName
import javax.swing.*


class SettingsComponent {
    @JvmField
    var mainPanel = JPanel()

    @JvmField
    var page = PageSetting()

    @JvmField
    var component = ComponentSetting()

    @JvmField
    var custom = CustomSetting()


    init {

        val temp = JPanel(migLayout()).apply {
            border = IdeBorderFactory.createTitledBorder("Component")

            add(JLabel("settings.label.translation.engine"))

            val body = JPanel(migLayout()).apply {
                add(JBTextField(20), wrap())
            }

            add(body, fillX())
        }

        mainPanel.layout = migLayoutVertical()
        mainPanel.add(temp, fillX())
        mainPanel.add(JPanel(), fillY())

//        mainPanel = FormBuilder.createFormBuilder()
//            .addLabeledComponent(JPanel().apply {
//                border = IdeBorderFactory.createTitledBorder("Component")
//            }, JPanel())
//            .addLabeledComponent(JBLabel("LogicName: "), page.logic)
//            .addLabeledComponent(JBLabel("StateName: "), page.state)
//            .addLabeledComponent(JBLabel("ViewName: "), page.view)
//            .addLabeledComponent(JBLabel("ViewFileName: "), page.viewFile)
//            .addLabeledComponent(JBLabel(""), dealMargin(JPanel()))
//            .addLabeledComponent(JBLabel("Template-Component"), JPanel())
//            .addLabeledComponent(JBLabel("LogicName: "), component.logic)
//            .addLabeledComponent(JBLabel("StateName: "), component.state)
//            .addLabeledComponent(JBLabel("ViewName: "), component.view)
//            .addLabeledComponent(JBLabel("ViewFileName: "), component.viewFile)
//            .addLabeledComponent(JBLabel(""), dealMargin(JPanel()))
//            .addLabeledComponent(JBLabel("Template-Custom"), JPanel())
//            .addLabeledComponent(JBLabel("LogicName: "), custom.logic)
//            .addLabeledComponent(JBLabel("StateName: "), custom.state)
//            .addLabeledComponent(JBLabel("ViewName: "), custom.view)
//            .addLabeledComponent(JBLabel("ViewFileName: "), custom.viewFile)
//            .addComponentFillVertically(JPanel(), 0)
//            .panel
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

class PageSetting {
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

fun fillX(): CC = CC().growX().pushX()
fun fillY(): CC = CC().growY().pushY()
fun wrap(): CC = CC().wrap()
fun migLayout() =
    MigLayout(LC().fill().gridGap("0!", "0!").insets("0"))
fun migLayoutVertical() =
    MigLayout(LC().flowY().fill().gridGap("0!", "0!").insets("0"))
fun fill(): CC = CC().grow().push()
