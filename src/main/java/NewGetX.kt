import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import com.intellij.ui.JBColor
import helper.DataService
import helper.GetXConfig
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.*
import java.util.*
import javax.swing.*


class NewGetX : AnAction() {
    private var project: Project? = null
    private lateinit var psiPath: String
    private lateinit var data: DataService

    /**
     * Overall popup entity
     */
    private lateinit var jDialog: JDialog
    private lateinit var nameTextField: JTextField
    private lateinit var templateGroup: ButtonGroup

    /**
     * select Function
     */
    private lateinit var folderBox: JCheckBox
    private lateinit var prefixBox: JCheckBox
    private lateinit var disposeBox: JCheckBox
    private lateinit var lifecycleBox: JCheckBox
    private lateinit var bindingBox: JCheckBox

    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString()
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1)
        initData()
        initView()
    }

    private fun initData() {
        data = DataService.instance
        jDialog = JDialog(JFrame(), "GetX Template Code Produce")
    }

    private fun initView() {
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
     * generate  file
     */
    private fun save() {
        if (nameTextField.text == null || "" == nameTextField.text.trim { it <= ' ' }) {
            Messages.showInfoMessage(project, "Please input the module name", "Info")
            return
        }
        dispose()
        //Create a file
        createFile()
        //Refresh project
        project?.guessProjectDir()?.refresh(false, true)
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

    private fun createFile() {
        val type = templateGroup.selection.actionCommand
        //deal default value
        if (GetXConfig.defaultModelName == type) {
            data.defaultMode = 0
        } else if (GetXConfig.easyModelName == type) {
            data.defaultMode = 1
        }
        data.useFolder = folderBox.isSelected
        data.usePrefix = prefixBox.isSelected
        data.autoDispose = disposeBox.isSelected
        data.addLifecycle = lifecycleBox.isSelected
        data.addBinding = bindingBox.isSelected
        val name = upperCase(nameTextField.text)
        val prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)
        var folder = ""
        var prefixName = ""

        //Add folder
        if (data.useFolder) {
            folder = "/$prefix"
        }

        //Add prefix
        if (data.usePrefix) {
            prefixName = "${prefix}_"
        }
        val path = psiPath + folder
        when (type) {
            GetXConfig.defaultModelName -> generateDefault(name, path, prefixName)
            GetXConfig.easyModelName -> generateEasy(name, path, prefixName)
        }
        //Add binding file
        if (data.addBinding) {
            generateFile(name, "binding.dart", path, "${prefixName}binding.dart")
        }
    }

    private fun generateDefault(name: String, path: String, prefixName: String) {
        generateFile(name, "state.dart", path, "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart")
        generateFile(name, "logic.dart", path, "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart")
        generateFile(name, "view.dart", path, "$prefixName${data.viewFileName.lowercase(Locale.getDefault())}.dart")
    }

    private fun generateEasy(name: String, path: String, prefixName: String) {
        generateFile(
                name,
                "easy/logic.dart",
                path,
                "${prefixName}${data.logicName.lowercase(Locale.getDefault())}.dart"
        )
        generateFile(
                name,
                "easy/view.dart",
                path,
                "${prefixName}${data.viewFileName.lowercase(Locale.getDefault())}.dart"
        )
    }

    private fun generateFile(name: String, inputFileName: String, filePath: String, outFileName: String) {
        //content deal
        val content = dealContent(name, inputFileName, outFileName)

        //Write file
        try {
            val folder = File(filePath)
            // if file doesnt exists, then create it
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File("$filePath/$outFileName")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(content)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //content need deal
    private fun dealContent(name: String, inputFileName: String, outFileName: String): String {
        //deal auto dispose
        var defaultFolder = "/templates/"
        if (data.autoDispose && inputFileName.contains("view.dart")) {
            defaultFolder += "auto/"
        }

        //add lifecycle
        if (data.addLifecycle && inputFileName.contains("logic.dart")) {
            defaultFolder += "lifecycle/"
        }

        //read file
        var content = ""
        try {
            val input = this.javaClass.getResourceAsStream("$defaultFolder$inputFileName")
            content = String(readStream(input!!))
        } catch (e: Exception) {
            //some error
        }
        var prefixName = ""
        //Adding a prefix requires modifying the imported class name
        if (data.usePrefix) {
            prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)}_"
        }

        //replace binding
        if (outFileName.contains("binding") && data.addBinding) {
            content = content.replace("Logic".toRegex(), data.logicName)
            content = content.replace(
                    "logic.dart".toRegex(),
                    "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart"
            )
        }
        //replace view about addBinding
        if (outFileName.contains(data.viewFileName.lowercase(Locale.getDefault())) && data.addBinding) {
            content = content.replace(
                    "@nameLogic logic = Get.put\\(@nameLogic\\(\\)\\)".toRegex(),
                    "logic = Get.find<@nameLogic>()"
            )
        }
        //replace logic
        if (outFileName.contains(data.logicName.lowercase(Locale.getDefault()))) {
            content = content.replace(
                    "state.dart".toRegex(),
                    "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart"
            )
            content = content.replace("Logic".toRegex(), data.logicName)
            content = content.replace("State".toRegex(), data.stateName)
            content = content.replace("state".toRegex(), data.stateName.lowercase(Locale.getDefault()))
        }
        //replace state
        if (outFileName.contains(data.stateName.lowercase(Locale.getDefault()))) {
            content = content.replace("State".toRegex(), data.stateName)
        }
        //replace view
        if (outFileName.contains(data.viewFileName.lowercase(Locale.getDefault()))) {
            content = content.replace(
                    "logic.dart".toRegex(),
                    "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart"
            )
            content = content.replace(
                    "state.dart".toRegex(),
                    "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart"
            )
            content = content.replace("Page".toRegex(), data.viewName)
            content = content.replace("Logic".toRegex(), data.logicName)
            content = content.replace("logic".toRegex(), data.logicName.lowercase(Locale.getDefault()))
            content = content.replace("@nameState".toRegex(), "@name${data.stateName}")
            content = content.replace("state".toRegex(), data.stateName.lowercase(Locale.getDefault()))
        }
        content = content.replace("@name".toRegex(), name)
        return content
    }

    @Throws(Exception::class)
    private fun readStream(inStream: InputStream): ByteArray {
        val outSteam = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len: Int
            while (inStream.read(buffer).apply { len = this } != -1) {
                outSteam.write(buffer, 0, len)
                println(String(buffer))
            }
        } catch (e: IOException) {
        } finally {
            outSteam.close()
            inStream.close()
        }
        return outSteam.toByteArray()
    }

    private val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) save()
            if (e.keyCode == KeyEvent.VK_ESCAPE) dispose()
        }

        override fun keyReleased(e: KeyEvent) {}
    }
    private val actionListener = ActionListener { e ->
        if (e.actionCommand == "Cancel") {
            dispose()
        } else {
            save()
        }
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

    private fun upperCase(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }

    private fun dispose() {
        jDialog.dispose()
    }
}
