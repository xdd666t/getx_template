package setting

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBTextField
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel


class SettingsComponent {
    @JvmField
    var mainPanel = JPanel()

    @JvmField
    var page = Setting()

    @JvmField
    var component = Setting()

    @JvmField
    var custom = Setting()


    init {
        val pageLayout = dealLayout(title = "Page", data = page)
        val componentLayout = dealLayout(title = "Component", data = component)
        val customLayout = dealLayout(title = "Custom", data = custom)

        mainPanel.layout = migLayoutVertical()
        mainPanel.add(pageLayout, fillX())
        mainPanel.add(componentLayout, fillX())
        mainPanel.add(customLayout, fillX())
        mainPanel.add(JPanel(), fillY())
    }

    private fun dealLayout(title: String, data: Setting): JPanel {
        return JPanel().apply {
            layout = migLayout()
            border = IdeBorderFactory.createTitledBorder(title)

            val body = JPanel().apply {
                layout = GridLayout(2, 2)

                add(dealItem(title = "ViewName", jbTextField = data.view, padding = 20))

                add(dealItem(title = "LogicName", jbTextField = data.logic, padding = 10))

                add(dealItem(title = "ViewFileName", jbTextField = data.viewFile, padding = 20))

                add(dealItem(title = "StateName", jbTextField = data.state, padding = 10))
            }
            add(body, fillX())
        }
    }

    private fun dealItem(title: String, jbTextField: JBTextField, padding: Int): JPanel {
        return JPanel().apply {
            layout = migLayout()
            border = BorderFactory.createEmptyBorder(0, 0, 15, 80)

            add(JPanel().apply {
                layout = BorderLayout()
                preferredSize = Dimension(70 + padding, 30)
                //left:WEST   right:EAST  top:NORTH  bottom:SOUTH  center:CENTER
                add(JLabel(title), BorderLayout.WEST)
            })

            add(jbTextField, fillX())
        }
    }


}

class Setting {
    var logic = JBTextField()

    var state = JBTextField()

    var view = JBTextField()

    var viewFile = JBTextField()
}


fun fillX(): CC = CC().growX().pushX()
fun fillY(): CC = CC().growY().pushY()
fun migLayout() =
    MigLayout(LC().fill().gridGap("0!", "0!").insets("0"))

fun migLayoutVertical() =
    MigLayout(LC().flowY().fill().gridGap("0!", "0!").insets("0"))
