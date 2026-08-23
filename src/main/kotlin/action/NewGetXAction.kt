package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import helper.DataService
import helper.DartNameUtils
import helper.GenerationMode
import helper.GenerationOptions
import helper.GetXName
import helper.ModuleNameSuffix
import helper.TemplateInfo
import helper.TemplateKind
import java.io.IOException
import java.nio.charset.StandardCharsets

class NewGetXAction : AnAction() {
    private val data: DataService
        get() = DataService.instance

    private val templateGenerator = GetXTemplateGenerator()

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible =
            event.project != null && resolveTargetDirectory(event) != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val targetDirectory = resolveTargetDirectory(event)
        if (targetDirectory == null) {
            Messages.showErrorDialog(
                project,
                "Please select a project directory before creating a GetX module.",
                "GetX",
            )
            return
        }

        NewGetXView(object : GetXListener {
            override fun onSave(view: NewGetXView): Boolean {
                return save(project, targetDirectory, view)
            }
        })
    }

    private fun save(
        project: Project,
        targetDirectory: VirtualFile,
        view: NewGetXView,
    ): Boolean {
        val moduleName = DartNameUtils.normalizeModuleName(view.nameTextField.text)
        if (moduleName == null) {
            Messages.showErrorDialog(
                project,
                "Module name must start with a letter and contain only letters, numbers, spaces, '_' or '-'.",
                "Invalid Module Name",
            )
            return false
        }

        val options = createOptions(view)
        val generatedFiles = try {
            templateGenerator.generate(moduleName, options)
        } catch (exception: IllegalArgumentException) {
            Messages.showErrorDialog(project, exception.message ?: "Invalid GetX configuration.", "GetX")
            return false
        } catch (exception: IllegalStateException) {
            Messages.showErrorDialog(project, exception.message ?: "Unable to load GetX templates.", "GetX")
            return false
        }

        if (!writeGeneratedFiles(project, targetDirectory, generatedFiles)) {
            return false
        }

        persistOptions(options)
        return true
    }

    private fun createOptions(view: NewGetXView): GenerationOptions {
        val mode = if (view.modeGroup.selection?.actionCommand == GetXName.ModeEasy) {
            GenerationMode.EASY
        } else {
            GenerationMode.DEFAULT
        }
        val templateKind = when (view.templateGroup.selection?.actionCommand) {
            GetXName.templateComponent -> TemplateKind.COMPONENT
            GetXName.templateCustom -> TemplateKind.CUSTOM
            else -> TemplateKind.PAGE
        }
        val template = templateFor(templateKind)

        val function = data.function.copy(
            useGetX5 = view.getX5Box.isSelected,
            useFolder = view.folderBox.isSelected,
            usePrefix = view.prefixBox.isSelected,
            isPageView = view.pageViewBox.isSelected,
            autoDispose = view.disposeBox.isSelected,
            addLifecycle = view.lifecycleBox.isSelected,
            addBinding = view.bindingBox.isSelected,
            funTabIndex = view.selectedFunctionTabIndex,
            logicInBuild = view.logicInBuildBox.isSelected,
            useScaffold = view.useScaffoldBox.isSelected,
        )
        val module = ModuleNameSuffix(
            logicName = template.logic,
            viewName = template.view,
            viewFileName = template.viewFile,
            stateName = template.state,
        )

        return GenerationOptions(
            mode = mode,
            function = function,
            module = module,
            setting = data.setting.copy(),
            templateKind = templateKind,
        )
    }

    private fun templateFor(kind: TemplateKind): TemplateInfo {
        return when (kind) {
            TemplateKind.PAGE -> data.templatePage
            TemplateKind.COMPONENT -> data.templateComponent
            TemplateKind.CUSTOM -> data.templateCustom
        }
    }

    private fun persistOptions(options: GenerationOptions) {
        data.modeDefault = options.mode == GenerationMode.DEFAULT
        data.modeEasy = options.mode == GenerationMode.EASY
        data.function = options.function.copy()
        data.module = options.module.copy()
        data.templatePage.selected = options.templateKind == TemplateKind.PAGE
        data.templateComponent.selected = options.templateKind == TemplateKind.COMPONENT
        data.templateCustom.selected = options.templateKind == TemplateKind.CUSTOM
    }

    private fun writeGeneratedFiles(
        project: Project,
        targetDirectory: VirtualFile,
        generatedFiles: List<GeneratedFile>,
    ): Boolean {
        val conflicts = generatedFiles
            .mapNotNull { file ->
                findChild(targetDirectory, file.relativePath)?.let { file.relativePath }
            }
            .distinct()

        if (conflicts.isNotEmpty()) {
            Messages.showErrorDialog(
                project,
                "The following files already exist and were not overwritten:\n${conflicts.joinToString("\n")}",
                "GetX Files Already Exist",
            )
            return false
        }

        var failure: Exception? = null
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                generatedFiles.forEach { file ->
                    writeFile(targetDirectory, file)
                }
            } catch (exception: Exception) {
                failure = exception
            }
        }

        if (failure != null) {
            Messages.showErrorDialog(
                project,
                "Unable to create GetX files: ${failure?.message ?: "unknown error"}",
                "GetX",
            )
            return false
        }

        targetDirectory.refresh(false, true)
        return true
    }

    private fun writeFile(targetDirectory: VirtualFile, generatedFile: GeneratedFile) {
        val parts = generatedFile.relativePath.split('/')
        val fileName = parts.last()
        var directory = targetDirectory

        parts.dropLast(1).forEach { part ->
            val existing = directory.findChild(part)
            directory = when {
                existing == null -> directory.createChildDirectory(this, part)
                existing.isDirectory -> existing
                else -> throw IOException("Path component is not a directory: $part")
            }
        }

        if (directory.findChild(fileName) != null) {
            throw IOException("File was created while generation was in progress: $fileName")
        }

        directory.createChildData(this, fileName)
            .setBinaryContent(generatedFile.content.toByteArray(StandardCharsets.UTF_8))
    }

    private fun findChild(targetDirectory: VirtualFile, relativePath: String): VirtualFile? {
        var current = targetDirectory
        relativePath.split('/').forEach { part ->
            if (!current.isDirectory) {
                return current
            }
            current = current.findChild(part) ?: return null
        }
        return current
    }

    private fun resolveTargetDirectory(event: AnActionEvent): VirtualFile? {
        val selectedFile = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return null
        return if (selectedFile.isDirectory) selectedFile else selectedFile.parent
    }
}
