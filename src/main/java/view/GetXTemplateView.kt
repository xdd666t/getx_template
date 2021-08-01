package view

import com.intellij.ui.JBColor
import helper.DataService
import helper.GetXConfig
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*

class GetXTemplateView(private val getXListener: GetXListener) {
    private var data: DataService = DataService.instance

    /**
     * Overall popup entity
     */
    private var jDialog: JDialog = JDialog(JFrame(), "GetX Template Code Produce")
    lateinit var nameTextField: JTextField
    lateinit var templateGroup: ButtonGroup

    /**
     * select Function
     */
    lateinit var folderBox: JCheckBox
    lateinit var prefixBox: JCheckBox
    lateinit var disposeBox: JCheckBox
    lateinit var lifecycleBox: JCheckBox
    lateinit var bindingBox: JCheckBox


    private val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) confirm()
            if (e.keyCode == KeyEvent.VK_ESCAPE) dispose()
        }

        override fun keyReleased(e: KeyEvent) {}
    }
    private val actionListener = ActionListener { e ->
        if (e.actionCommand == "Cancel") {
            dispose()
        } else {
            confirm()
        }
    }

    init {
        //Set function button
        val container = jDialog.contentPane
        container.layout = BoxLayout(container, BoxLayout.Y_AXIS)

        //Set the main module style: mode, function
        //deal default value
        setMode(container)

        //deal setting about file
        setFunction(container)

        //Generate module name and OK cancel button
        setModuleAndConfirm(container)

        //Choose a pop-up style
        setJDialog()
    }

    /**
     * Main module
     */
    private fun setMode(container: Container) {
        //Two rows and two columns
        val template = JPanel()
        template.layout = GridLayout(1, 2)
        //Set the main module style：mode, function
        template.border = BorderFactory.createTitledBorder("Select Mode")

        //default model
        val defaultBtn = JRadioButton(GetXConfig.defaultModelName, data.defaultMode == 0)
        defaultBtn.actionCommand = GetXConfig.defaultModelName
        setPadding(defaultBtn)
        template.add(defaultBtn)

        //easy model
        val easyBtn = JRadioButton(GetXConfig.easyModelName, data.defaultMode == 1)
        easyBtn.actionCommand = GetXConfig.easyModelName
        setPadding(easyBtn)
        template.add(easyBtn)

        templateGroup = ButtonGroup()
        templateGroup.add(defaultBtn)
        templateGroup.add(easyBtn)

        container.add(template)
        setSpacing(container)
    }

    /**
     * Generate file
     */
    private fun setFunction(container: Container) {
        //Select build file
        val function = JPanel()
        function.layout = GridLayout(3, 2)
        function.border = BorderFactory.createTitledBorder("Select Function")

        //use folder
        folderBox = JCheckBox("useFolder", data.useFolder)
        setMargin(folderBox)
        function.add(folderBox)

        //use prefix
        prefixBox = JCheckBox("usePrefix", data.usePrefix)
        setMargin(prefixBox)
        function.add(prefixBox)

        //auto dispose
        disposeBox = JCheckBox("autoDispose", data.autoDispose)
        setMargin(disposeBox)
        function.add(disposeBox)

        //add lifecycle
        lifecycleBox = JCheckBox("addLifecycle", data.addLifecycle)
        setMargin(lifecycleBox)
        function.add(lifecycleBox)

        //add binding
        bindingBox = JCheckBox("addBinding", data.addBinding)
        setMargin(bindingBox)
        function.add(bindingBox)

        container.add(function)
        setSpacing(container)
    }

    /**
     * Generate file name and button
     */
    private fun setModuleAndConfirm(container: Container) {
        //input module name
        val nameField = JPanel()
        nameField.layout = FlowLayout()
        nameField.border = BorderFactory.createTitledBorder("Module Name")
        nameTextField = JTextField(28)
        nameTextField.addKeyListener(keyListener)
        nameField.add(nameTextField)

        container.add(nameField)
        setSpacing(container)

        //OK cancel button
        val cancel = JButton("Cancel")
        cancel.foreground = JBColor.RED
        cancel.addActionListener(actionListener)
        val ok = JButton("OK")
        ok.foreground = JBColor.GREEN
        ok.addActionListener(actionListener)

        val menu = JPanel()
        menu.layout = FlowLayout()
        menu.add(cancel)
        menu.add(ok)
        container.add(menu)
    }

    /**
     * Set the overall pop-up style
     */
    private fun setJDialog() {
        //The focus is on the current pop-up window,
        // and the focus will not shift even if you click on other areas
        jDialog.isModal = true
        //Set padding
        (jDialog.contentPane as JPanel).border = BorderFactory.createEmptyBorder(10, 10, 15, 10)
        jDialog.setSize(430, 400)
        jDialog.setLocationRelativeTo(null)
        jDialog.isVisible = true
    }

    private fun setPadding(btn: JRadioButton) {
        btn.border = BorderFactory.createEmptyBorder(5, 10, 10, 0)
    }

    private fun setMargin(btn: JCheckBox) {
        btn.border = BorderFactory.createEmptyBorder(5, 10, 10, 0)
    }

    private fun setSpacing(container: Container) {
        container.add(JPanel())
    }

    private fun confirm() {
        getXListener.onData(this)

        if (getXListener.onSave()) {
            dispose()
        }
    }

    private fun dispose() {
        jDialog.dispose()
    }
}

interface GetXListener {
    fun onSave(): Boolean

    fun onData(view: GetXTemplateView)
}