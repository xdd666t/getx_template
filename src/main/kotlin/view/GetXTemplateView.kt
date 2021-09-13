package view

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBTabbedPane
import helper.DataService
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
    lateinit var modeGroup: ButtonGroup

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

    /**
     * select Template：Template Function
     */
    lateinit var templateGroup: ButtonGroup


    private val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}

        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) confirm()
            if (e.keyCode == KeyEvent.VK_ESCAPE) dispose()
        }

        override fun keyReleased(e: KeyEvent) {}
    }

    private val actionChangeListener = ActionListener {
        //data change
        getXListener.onDataChange(this)

        //click btn
        if (it.actionCommand == "Cancel") {
            dispose()
        } else if (it.actionCommand == "OK") {
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

        //deal main function, minor function, template function
        val main = getMainFunction()
        val minor = getMinorFunction()
        val template = getTemplateFunction()
        setFunctionTab(main = main, minor = minor, template = template, container = container)

        //Generate module name and ok cancel button
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
        val defaultBtn = JRadioButton(data.modeDefault.name, data.modeDefault.selected)
        defaultBtn.actionCommand = data.modeDefault.name
        defaultBtn.addActionListener(actionChangeListener)
        setBtnPadding(defaultBtn)
        template.add(defaultBtn)

        //easy model
        val easyBtn = JRadioButton(data.modeEasy.name, data.modeEasy.selected)
        easyBtn.actionCommand = data.modeEasy.name
        easyBtn.addActionListener(actionChangeListener)
        setBtnPadding(easyBtn)
        template.add(easyBtn)

        modeGroup = ButtonGroup()
        modeGroup.add(defaultBtn)
        modeGroup.add(easyBtn)

        container.add(template)
        setSpacing(container)
    }

    /**
     * Generate file
     */
    private fun getMainFunction(): JPanel {
        //Main Function
        val main = JPanel()
        main.layout = GridLayout(2, 2)

        //use folder
        folderBox = JCheckBox("useFolder", data.useFolder)
        folderBox.addActionListener(actionChangeListener)
        setMargin(folderBox)
        main.add(folderBox)

        //use prefix
        prefixBox = JCheckBox("usePrefix", data.usePrefix)
        prefixBox.addActionListener(actionChangeListener)
        setMargin(prefixBox)
        main.add(prefixBox)

        //pageView
        pageViewBox = JCheckBox("isPageView", data.isPageView)
        pageViewBox.addActionListener(actionChangeListener)
        setMargin(pageViewBox)
        main.add(pageViewBox)

        //add binding
        bindingBox = JCheckBox("addBinding", data.addBinding)
        bindingBox.addActionListener(actionChangeListener)
        setMargin(bindingBox)
        main.add(bindingBox)

        return main
    }


    private fun getMinorFunction(): JPanel {
        //Minor Function
        val minor = JPanel()
        minor.layout = GridLayout(2, 2)

        //add lifecycle
        lifecycleBox = JCheckBox("addLifecycle", data.addLifecycle)
        lifecycleBox.addActionListener(actionChangeListener)
        setMargin(lifecycleBox)
        minor.add(lifecycleBox)

        //auto dispose
        disposeBox = JCheckBox("autoDispose", data.autoDispose)
        disposeBox.addActionListener(actionChangeListener)
        setMargin(disposeBox)
        minor.add(disposeBox)

        //support lint normal
        lintNormBox = JCheckBox("lintNorm", data.lintNorm)
        lintNormBox.addActionListener(actionChangeListener)
        setMargin(lintNormBox)
        minor.add(lintNormBox)

        return minor
    }

    private fun getTemplateFunction(): JPanel {
        //Minor Function
        val template = JPanel()
        template.layout = GridLayout(2, 2)

        //add page
        val pageBtn = JRadioButton(data.templatePage.name, data.templatePage.selected)
        pageBtn.actionCommand = data.templatePage.name
        pageBtn.addActionListener(actionChangeListener)
        setBtnPadding(template = pageBtn)
        template.add(pageBtn)

        //add component
        val componentBtn = JRadioButton(data.templateComponent.name, data.templateComponent.selected)
        componentBtn.actionCommand = data.templateComponent.name
        componentBtn.addActionListener(actionChangeListener)
        setBtnPadding(template = componentBtn)
        template.add(componentBtn)

        //add custom
        val customBtn = JRadioButton(data.templateCustom.name, data.templateCustom.selected)
        customBtn.actionCommand = data.templateCustom.name
        customBtn.addActionListener(actionChangeListener)
        setBtnPadding(template = customBtn)
        template.add(customBtn)

        templateGroup = ButtonGroup()
        templateGroup.add(pageBtn)
        templateGroup.add(componentBtn)
        templateGroup.add(customBtn)

        //empty placeholder
        template.add(JPanel())

        return template
    }

    private fun setFunctionTab(main: JPanel, minor: JPanel, template: JPanel, container: Container) {
        val function = JPanel()
        function.border = BorderFactory.createTitledBorder("Select Function")

        //add tab
        val tab = JBTabbedPane()
        tab.addTab("Main", main)
        tab.addTab("Minor", minor)
        tab.addTab("Template", template)
        tab.addChangeListener {
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
        nameTextField = JTextField(33)
        nameTextField.addKeyListener(keyListener)
        tempTextField.add(nameTextField)
        nameField.add(tempTextField)
        container.add(nameField)

        //OK cancel button
        val cancel = JButton("Cancel")
        cancel.foreground = JBColor.RED
        cancel.addActionListener(actionChangeListener)
        val ok = JButton("OK")
        ok.foreground = JBColor.BLUE
        ok.addActionListener(actionChangeListener)
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

    private fun setBtnPadding(mode: JRadioButton = JRadioButton(), template: JRadioButton = JRadioButton()) {
        mode.border = BorderFactory.createEmptyBorder(5, 10, 10, 100)
        template.border = BorderFactory.createEmptyBorder(5, 0, 10, 100)
    }

    private fun setMargin(box: JCheckBox) {
        box.border = BorderFactory.createEmptyBorder(5, 0, 10, 100)
    }

    private fun setSpacing(container: Container) {
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createEmptyBorder(0, 0, 3, 0)
        container.add(jPanel)
    }

    private fun confirm() {
        //data change, deal TextField listener
        getXListener.onDataChange(this)

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

    fun onDataChange(view: GetXTemplateView)
}