import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import helper.DataService
import helper.GetXConfig
import view.GetXListener
import view.GetXTemplateView
import java.io.*
import java.util.*


class NewGetX : AnAction() {
    private var project: Project? = null
    private lateinit var psiPath: String
    private var data: DataService = DataService.instance

    /**
     * module name
     */
    private lateinit var moduleName: String


    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString()
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1)
        initView()
    }

    private fun initView() {
        GetXTemplateView(object : GetXListener {
            override fun onSave(): Boolean {
                return save()
            }

            override fun onData(view: GetXTemplateView) {
                moduleName = view.nameTextField.text
                val modelType = view.templateGroup.selection.actionCommand

                //deal default value
                if (GetXConfig.defaultModelName == modelType) {
                    data.defaultMode = 0
                } else if (GetXConfig.easyModelName == modelType) {
                    data.defaultMode = 1
                }
                data.useFolder = view.folderBox.isSelected
                data.usePrefix = view.prefixBox.isSelected
                data.autoDispose = view.disposeBox.isSelected
                data.addLifecycle = view.lifecycleBox.isSelected
                data.addBinding = view.bindingBox.isSelected
            }
        })
    }

    /**
     * generate  file
     */
    private fun save(): Boolean {
        if ("" == moduleName.trim { it <= ' ' }) {
            Messages.showInfoMessage(project, "Please input the module name", "Info")
            return false
        }
        //Create a file
        createFile()
        //Refresh project
        project?.guessProjectDir()?.refresh(false, true)

        return true
    }

    private fun createFile() {

        val name = upperCase(moduleName)
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
        when (data.defaultMode) {
            0 -> generateDefault(name, path, prefixName)
            1 -> generateEasy(name, path, prefixName)
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

    private fun upperCase(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }
}
