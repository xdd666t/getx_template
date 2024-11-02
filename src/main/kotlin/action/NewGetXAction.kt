package action

import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import helper.DataService
import helper.GetXName
import helper.TemplateInfo
import java.io.*
import java.util.*
import kotlin.collections.HashMap


class NewGetXAction : AnAction() {
    private var project: Project? = null
    private lateinit var psiPath: String
    private var data = DataService.instance

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
        NewGetXView(object : GetXListener {
            override fun onSave(): Boolean {
                return save()
            }

            override fun onDataChange(view: NewGetXView) {
                //module name
                moduleName = view.nameTextField.text

                //deal default value
                val modelType = view.modeGroup.selection.actionCommand
                data.modeDefault = (GetXName.ModeDefault == modelType)
                data.modeEasy = (GetXName.ModeEasy == modelType)

                //function area
                data.function.useGetX5 = view.getX5Box.isSelected
                data.function.useFolder = view.folderBox.isSelected
                data.function.usePrefix = view.prefixBox.isSelected
                data.function.isPageView = view.pageViewBox.isSelected
                data.function.autoDispose = view.disposeBox.isSelected
                data.function.addLifecycle = view.lifecycleBox.isSelected
                data.function.addBinding = view.bindingBox.isSelected
                data.function.lintNorm = view.lintNormBox.isSelected
                val templateType = view.templateGroup.selection.actionCommand
                val list = ArrayList<TemplateInfo>().apply {
                    add(data.templatePage.apply { selected = (GetXName.templatePage == templateType) })
                    add(data.templateComponent.apply { selected = (GetXName.templateComponent == templateType) })
                    add(data.templateCustom.apply { selected = (GetXName.templateCustom == templateType) })
                }
                for (item in list) {
                    if (!item.selected) continue
                    data.module.logicName = item.logic
                    data.module.stateName = item.state
                    data.module.viewName = item.view
                    data.module.viewFileName = item.viewFile
                    break
                }
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

        //add folder
        if (data.function.useFolder) {
            folder = "/$prefix"

            //use folder suffix
            if (data.setting.useFolderSuffix) {
                folder = "${folder}_${data.module.viewName.lowercase()}"
            }
        }

        //add prefix
        if (data.function.usePrefix) {
            prefixName = "${prefix}_"
        }

        //select generate file mode
        val path = psiPath + folder
        if (data.modeDefault) {
            generateDefault(path, prefixName)
        } else if (data.modeEasy) {
            generateEasy(path, prefixName)
        }

        //add binding file
        if (data.function.addBinding) {
            val inputFileName = if (data.function.useGetX5) {
                "binding_5.dart"
            } else {
                "binding_4.dart"
            }
            generateFile(inputFileName, path, "${prefixName}binding.dart")
        }
    }

    private fun generateDefault(path: String, prefixName: String) {
        generateFile("state.dart", path, "$prefixName${data.module.stateName.lowercase(Locale.getDefault())}.dart")
        generateFile("logic.dart", path, "$prefixName${data.module.logicName.lowercase(Locale.getDefault())}.dart")
        generateFile("view.dart", path, "$prefixName${data.module.viewFileName.lowercase(Locale.getDefault())}.dart")
    }

    private fun generateEasy(path: String, prefixName: String) {
        generateFile(
            "easy/logic.dart", path, "${prefixName}${data.module.logicName.lowercase(Locale.getDefault())}.dart"
        )
        generateFile(
            "easy/view.dart", path, "${prefixName}${data.module.viewFileName.lowercase(Locale.getDefault())}.dart"
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

    private var replaceContentMap = HashMap<String, String>()

    //content need deal
    private fun dealContent(inputFileName: String): String {
        //module name
        val name = upperCase(moduleName)
        //Adding a prefix requires modifying the imported class name
        var prefixName = ""
        if (data.function.usePrefix) {
            prefixName = "${CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)}_"
        }

        //select suitable file, return suitable content
        var content = getSuitableContent(inputFileName)
        replaceContentMap.clear()

        //replace view file
        replaceView(inputFileName, prefixName)

        //replace logic file
        replaceLogic(inputFileName, prefixName)

        //replace binding file
        replaceBinding(inputFileName, prefixName)

        //replace state file
        replaceState(inputFileName)

        replaceContentMap["@name"] = name

        replaceContentMap.forEach { (key, value) ->
            content = content.replace(key.toRegex(), value)
        }

        return content
    }

    private fun replaceLogic(inputFileName: String, prefixName: String) {
        if (!inputFileName.contains("logic.dart")) {
            return
        }

        replaceContentMap["state.dart"] = "$prefixName${data.module.stateName.lowercase(Locale.getDefault())}.dart"
        replaceContentMap["Logic"] = data.module.logicName
        replaceContentMap["State"] = data.module.stateName
        replaceContentMap["state"] = data.module.stateName.lowercase(Locale.getDefault())
    }

    private fun replaceView(inputFileName: String, prefixName: String) {
        if (!inputFileName.contains("view.dart")) {
            return
        }

        // deal getX5
        if (data.function.useGetX5) {
            replaceContentMap["Get.find"] = "Bind.find"
            replaceContentMap["Get.delete"] = "Bind.delete"
        }

        //deal binding function
        if (data.function.addBinding) {
            replaceContentMap["Get.put\\(@nameLogic\\(\\)\\)"] = "Get.find<@nameLogic>()"
        }

        //remove lint
        if (!data.function.lintNorm || (!data.setting.lint && data.function.lintNorm)) {
            replaceContentMap["\\s*\nimport 'state.dart';"] = ""
            replaceContentMap["final @nameLogic"] = "final"
            replaceContentMap["final @nameState"] = "final"
        }
        //remove flutter_lints
        if (!data.function.lintNorm || (!data.setting.flutterLints && data.function.lintNorm)) {
            replaceContentMap["const @namePage\\(\\{Key\\? key}\\) : super\\(key: key\\);\\s*\n\\s\\s"] = ""
            replaceContentMap["@namePage\\(\\{Key\\? key}\\) : super\\(key: key\\);\\s*\n\\s\\s"] = ""
        }

        //deal suffix of custom module name
        replaceContentMap["logic.dart"] = "$prefixName${data.module.logicName.lowercase(Locale.getDefault())}.dart"
        replaceContentMap["state.dart"] = "$prefixName${data.module.stateName.lowercase(Locale.getDefault())}.dart"

        replaceContentMap["Page"] = data.module.viewName
        replaceContentMap["Logic"] = data.module.logicName
        replaceContentMap["logic"] = data.module.logicName.lowercase(Locale.getDefault())
        replaceContentMap["@nameState"] = "@name${data.module.stateName}"
        replaceContentMap["state"] = data.module.stateName.lowercase(Locale.getDefault())
    }

    private fun replaceState(inputFileName: String) {
        if (!inputFileName.contains("state.dart")) {
            return
        }

        replaceContentMap["State"] = data.module.stateName
    }

    private fun replaceBinding(inputFileName: String, prefixName: String) {
        if (!inputFileName.contains("binding_4.dart") && !inputFileName.contains("binding_5.dart")) {
            return
        }

        if (data.function.addBinding) {
            replaceContentMap["Logic"] = data.module.logicName
            replaceContentMap["logic.dart"] = "$prefixName${data.module.logicName.lowercase(Locale.getDefault())}.dart"
        }
    }

    private fun getSuitableContent(inputFileName: String): String {
        //deal auto dispose or pageView
        var defaultFolder = "/templates/normal/"

        // view.dart
        if (inputFileName.contains("view.dart")) {
            if (data.function.autoDispose) {
                defaultFolder = "/templates/auto/"
            }

            if (data.function.isPageView) {
                defaultFolder = "/templates/pageView/"
            }
        }

        //add lifecycle
        if (data.function.addLifecycle && inputFileName.contains("logic.dart")) {
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
        } catch (_: IOException) {
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
