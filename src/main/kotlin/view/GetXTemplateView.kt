package view

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBTabbedPane
import helper.DataService
import helper.GetXConfig
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


open class GetXTemplateView(private val getXListener: GetXListener) {
    private var data: DataService = DataService.instance

    /**
     * Overall popup entity
     */
    private var jDialog: JDialog = JDialog(JFrame(), "GetX Template Code Produce")
    lateinit var nameTextField: JTextField
    lateinit var templateGroup: ButtonGroup

    /**
     * select Function：main Function
     */
    lateinit var folderBox: JCheckBox
    lateinit var prefixBox: JCheckBox
    lateinit var pageViewBox: JCheckBox
    lateinit var bindingBox: JCheckBox

    /**
     * select Function：minor Function
     */
    lateinit var disposeBox: JCheckBox
    lateinit var lifecycleBox: JCheckBox
    lateinit var lintNormBox: JCheckBox


    private val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}

        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) confirm()
            if (e.keyCode == KeyEvent.VK_ESCAPE) dispose()
        }

        override fun keyReleased(e: KeyEvent) {}
    }

    private val actionListener = ActionListener {
        if (it.actionCommand == "Cancel") {
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

        //deal main function, minor function
        val mainFunction = getMainFunction()
        val minorFunction = getMinorFunction()
        setFunctionTab(main = mainFunction, minor = minorFunction, container = container)

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
        setBtnPadding(defaultBtn)
        template.add(defaultBtn)

        //easy model
        val easyBtn = JRadioButton(GetXConfig.easyModelName, data.defaultMode == 1)
        easyBtn.actionCommand = GetXConfig.easyModelName
        setBtnPadding(easyBtn)
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
    private fun getMainFunction(): JPanel {
        //Main Function
        val mainFunction = JPanel()
        mainFunction.layout = GridLayout(2, 2)

        //use folder
        folderBox = JCheckBox("useFolder", data.useFolder)
        setMargin(folderBox)
        mainFunction.add(folderBox)

        //use prefix
        prefixBox = JCheckBox("usePrefix", data.usePrefix)
        setMargin(prefixBox)
        mainFunction.add(prefixBox)

        //pageView
        pageViewBox = JCheckBox("isPageView", data.isPageView)
        setMargin(pageViewBox)
        mainFunction.add(pageViewBox)

        //add binding
        bindingBox = JCheckBox("addBinding", data.addBinding)
        setMargin(bindingBox)
        mainFunction.add(bindingBox)

        return mainFunction
    }


    private fun getMinorFunction(): JPanel {
        //Minor Function
        val minorFunction = JPanel()
        minorFunction.layout = GridLayout(2, 2)

        //add lifecycle
        lifecycleBox = JCheckBox("addLifecycle", data.addLifecycle)
        setMargin(lifecycleBox)
        minorFunction.add(lifecycleBox)

        //auto dispose
        disposeBox = JCheckBox("autoDispose", data.autoDispose)
        setMargin(disposeBox)
        minorFunction.add(disposeBox)

        //support lint normal
        lintNormBox = JCheckBox("lintNorm", data.lintNorm)
        setMargin(lintNormBox)
        minorFunction.add(lintNormBox)

        //empty node
        minorFunction.add(JPanel())

        return minorFunction
    }

    private fun setFunctionTab(main: JPanel, minor: JPanel, container: Container) {
        val function = JPanel()
        function.border = BorderFactory.createTitledBorder("Select Function")

        // 添加选项卡
        val tab = JBTabbedPane()
        tab.addTab("Main", main)
        tab.addTab("Minor", minor)
        tab.addChangeListener{
            data.funTabIndex = tab.selectedIndex
        }
        tab.selectedIndex = data.funTabIndex

        function.add(tab)
        container.add(function)
        setSpacing(container)

        /// deal listener
        pageViewBox.addActionListener {
            if (disposeBox.isSelected && pageViewBox.isSelected) {
                disposeBox.isSelected = false
            }
        }
        disposeBox.addActionListener {
            if (disposeBox.isSelected && pageViewBox.isSelected) {
                pageViewBox.isSelected = false
            }
        }
    }

    /**
     * Generate file name and button
     */
    private fun setModuleAndConfirm(container: Container) {
        //input module name
        //Row：Box.createHorizontalBox() | Column：Box.createVerticalBox()
        //add Module Name
        val nameField = JPanel()
        val tempTextField = JPanel()
        tempTextField.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        nameField.border = BorderFactory.createTitledBorder("Module Name")
        nameTextField = JTextField(30)
        nameTextField.addKeyListener(keyListener)
        tempTextField.add(nameTextField)
        nameField.add(tempTextField)
        container.add(nameField)

        //OK cancel button
        val cancel = JButton("Cancel")
        cancel.foreground = JBColor.RED
        cancel.addActionListener(actionListener)
        val ok = JButton("OK")
        ok.foreground = JBColor.BLUE
        ok.addActionListener(actionListener)
        val menu = JPanel()
        menu.layout = FlowLayout()
        menu.add(cancel)
        menu.add(ok)
        menu.border = BorderFactory.createEmptyBorder(10, 0, 10, 0)

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
        (jDialog.contentPane as JPanel).border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        //auto layout
        jDialog.pack()
        jDialog.setLocationRelativeTo(null)
        jDialog.isVisible = true
    }

    private fun setBtnPadding(btn: JRadioButton) {
        btn.border = BorderFactory.createEmptyBorder(5, 10, 10, 100)
    }

    private fun setMargin(btn: JCheckBox) {
        btn.border = BorderFactory.createEmptyBorder(5, 0, 10, 100)
    }

    private fun setSpacing(container: Container) {
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createEmptyBorder(0, 0, 3, 0)
        container.add(jPanel)
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