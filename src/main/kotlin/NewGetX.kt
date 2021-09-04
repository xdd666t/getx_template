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

            override fun onDataChange(view: GetXTemplateView) {
                //module name
                moduleName = view.nameTextField.text

                //deal default value
                val modelType = view.templateGroup.selection.actionCommand
                if (GetXConfig.defaultModelName == modelType) {
                    data.defaultMode = 0
                } else if (GetXConfig.easyModelName == modelType) {
                    data.defaultMode = 1
                }

                //function area
                data.useFolder = view.folderBox.isSelected
                data.usePrefix = view.prefixBox.isSelected
                data.isPageView = view.pageViewBox.isSelected
                data.autoDispose = view.disposeBox.isSelected
                data.addLifecycle = view.lifecycleBox.isSelected
                data.addBinding = view.bindingBox.isSelected
                data.lintNorm = view.lintNormBox.isSelected
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
        val prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCase(moduleName))
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
            0 -> generateDefault(path, prefixName)
            1 -> generateEasy(path, prefixName)
        }
        //Add binding file
        if (data.addBinding) {
            generateFile("binding.dart", path, "${prefixName}binding.dart")
        }
    }

    private fun generateDefault(path: String, prefixName: String) {
        generateFile("state.dart", path, "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart")
        generateFile("logic.dart", path, "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart")
        generateFile("view.dart", path, "$prefixName${data.viewFileName.lowercase(Locale.getDefault())}.dart")
    }

    private fun generateEasy(path: String, prefixName: String) {
        generateFile(
            "easy/logic.dart",
            path,
            "${prefixName}${data.logicName.lowercase(Locale.getDefault())}.dart"
        )
        generateFile(
            "easy/view.dart",
            path,
            "${prefixName}${data.viewFileName.lowercase(Locale.getDefault())}.dart"
        )
    }

    private fun generateFile(inputFileName: String, filePath: String, outFileName: String) {
        //content deal
        val content = dealContent(inputFileName)

        //Write file
        try {
            val folder = File(filePath)
            // if file not exists, then create it
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
    private fun dealContent(inputFileName: String): String {
        //module name
        val name = upperCase(moduleName)
        //Adding a prefix requires modifying the imported class name
        var prefixName = ""
        if (data.usePrefix) {
            prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)}_"
        }

        //select suitable file, return suitable content
        var content = getSuitableContent(inputFileName)

        //replace view file
        content = replaceView(content, inputFileName, prefixName)

        //replace logic file
        content = replaceLogic(content, inputFileName, prefixName)

        //replace binding file
        content = replaceBinding(content, inputFileName, prefixName)

        //replace state file
        content = replaceState(content, inputFileName)

        content = content.replace("@name".toRegex(), name)

        return content
    }

    private fun replaceLogic(content: String, inputFileName: String, prefixName: String): String {
        var tempContent = content

        if (!inputFileName.contains("logic.dart")) {
            return tempContent
        }

        tempContent = tempContent.replace(
            "state.dart".toRegex(),
            "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart"
        )
        tempContent = tempContent.replace("Logic".toRegex(), data.logicName)
        tempContent = tempContent.replace("State".toRegex(), data.stateName)
        tempContent = tempContent.replace("state".toRegex(), data.stateName.lowercase(Locale.getDefault()))

        return tempContent
    }

    private fun replaceView(content: String, inputFileName: String, prefixName: String): String {
        var tempContent = content

        if (!inputFileName.contains("view.dart")) {
            return tempContent
        }

        //deal binding function
        if (data.addBinding) {
            tempContent = tempContent.replace("Get.put\\(@nameLogic\\(\\)\\)".toRegex(), "Get.find<@nameLogic>()")
        }

        //deal lint norm
        if (!data.lintNorm) {
            tempContent = tempContent.replace("\\s*\nimport 'state.dart';".toRegex(), "")
            tempContent = tempContent.replace("final @nameLogic".toRegex(), "final")
            tempContent = tempContent.replace("final @nameState".toRegex(), "final")
        }

        tempContent = tempContent.replace(
            "logic.dart".toRegex(),
            "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart"
        )
        tempContent = tempContent.replace(
            "state.dart".toRegex(),
            "$prefixName${data.stateName.lowercase(Locale.getDefault())}.dart"
        )
        tempContent = tempContent.replace("Page".toRegex(), data.viewName)
        tempContent = tempContent.replace("Logic".toRegex(), data.logicName)
        tempContent = tempContent.replace("logic".toRegex(), data.logicName.lowercase(Locale.getDefault()))
        tempContent = tempContent.replace("@nameState".toRegex(), "@name${data.stateName}")
        tempContent = tempContent.replace("state".toRegex(), data.stateName.lowercase(Locale.getDefault()))

        return tempContent
    }

    private fun replaceState(content: String, inputFileName: String): String {
        var tempContent = content

        if (!inputFileName.contains("state.dart")) {
            return tempContent
        }

        tempContent = tempContent.replace("State".toRegex(), data.stateName)

        return tempContent
    }

    private fun replaceBinding(content: String, inputFileName: String, prefixName: String): String {
        var tempContent = content

        if (!inputFileName.contains("binding.dart")) {
            return tempContent
        }

        if (data.addBinding) {
            tempContent = tempContent.replace("Logic".toRegex(), data.logicName)
            tempContent = tempContent.replace(
                "logic.dart".toRegex(),
                "$prefixName${data.logicName.lowercase(Locale.getDefault())}.dart"
            )
        }

        return tempContent
    }

    private fun getSuitableContent(inputFileName: String): String {
        //deal auto dispose or pageView
        var defaultFolder = "/templates/normal/"

        // view.dart
        if (inputFileName.contains("view.dart")) {
            if (data.autoDispose) {
                defaultFolder = "/templates/auto/"
            }

            if (data.isPageView) {
                defaultFolder = "/templates/pageView/"
            }
        }

        //add lifecycle
        if (data.addLifecycle && inputFileName.contains("logic.dart")) {
            defaultFolder = "/templates/lifecycle/"
        }

        //read file
        var content = ""
        try {
            val input = this.javaClass.getResourceAsStream("$defaultFolder$inputFileName")
            content = String(readStream(input!!))
        } catch (e: Exception) {
            //some error
        }

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
