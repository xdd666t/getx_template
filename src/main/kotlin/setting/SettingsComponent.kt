package setting

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.selected
import helper.DataService
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.*


class SettingsComponent {
    private val data = DataService.instance

    @JvmField
    var mainPanel = JPanel()

    @JvmField
    var page = Setting()

    @JvmField
    var component = Setting()

    @JvmField
    var custom = Setting()

    init {
        val functionLayout = selectFunctionLayout()
        val pageLayout = templateNameLayout(title = "Page", data = page)
        val componentLayout = templateNameLayout(title = "Component", data = component)
        val customLayout = templateNameLayout(title = "Custom", data = custom)

        mainPanel.layout = migLayoutVertical()
        mainPanel.add(functionLayout, fillX())
        mainPanel.add(pageLayout, fillX())
        mainPanel.add(componentLayout, fillX())
        mainPanel.add(customLayout, fillX())
        mainPanel.add(JPanel(), fillY())
    }

    private fun selectFunctionLayout(): JPanel {
        return JPanel().apply {
            layout = migLayout()
            border = IdeBorderFactory.createTitledBorder("SelectFunction")

            val body = JPanel().apply {
                layout = GridLayout(1, 2)

                //separate lintNorm：lint and flutter_lints
                add(lintNormFunction(), fillX())

                //open folder suffix
                add(useFolderSuffix(), fillX())
            }
            add(body, fillX())
        }
    }

    private fun useFolderSuffix(): JPanel {
        return JPanel().apply {
            layout = migLayout()

            add(JLabel().apply {
                text = "useFolderSuffix："
                border = BorderFactory.createEmptyBorder()
            })
            add(JCheckBox().apply {
                border = BorderFactory.createEmptyBorder()
                isSelected = data.setting.useFolderSuffix
                addActionListener {
                    data.setting.useFolderSuffix = isSelected
                }
            })

            add(JPanel(), fillX())
        }
    }

    private fun lintNormFunction(): JPanel {
        return JPanel().apply {
            layout = migLayout()

            add(JLabel("lintNorm："))
            add(JComboBox<String>().apply {
                addItem("all")
                addItem("flutter_lints")
                addItem("lint")
                preferredSize = Dimension(105, 30)
                selectedIndex = data.setting.lintNormIndex

                this.addActionListener {
                    data.setting.lintNormIndex = this.selectedIndex
                    data.setting.flutterLints = false
                    data.setting.lint = false
                    when (this.selectedIndex) {
                        0 -> {
                            data.setting.flutterLints = true
                            data.setting.lint = true
                        }
                        1 -> data.setting.flutterLints = true
                        2 -> data.setting.lint = true
                    }
                }
            })

            add(JPanel(), fillX())
        }
    }

    private fun templateNameLayout(title: String, data: Setting): JPanel {
        return JPanel().apply {
            layout = migLayout()
            border = IdeBorderFactory.createTitledBorder(title)

            val body = JPanel().apply {
                layout = GridLayout(2, 2)

                add(templateItem(title = "ViewName", jbTextField = data.view, padding = 20))

                add(templateItem(title = "LogicName", jbTextField = data.logic, padding = 10))

                add(templateItem(title = "ViewFileName", jbTextField = data.viewFile, padding = 20))

                add(templateItem(title = "StateName", jbTextField = data.state, padding = 10))
            }
            add(body, fillX())
        }
    }

    private fun templateItem(title: String, jbTextField: JBTextField, padding: Int): JPanel {
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
