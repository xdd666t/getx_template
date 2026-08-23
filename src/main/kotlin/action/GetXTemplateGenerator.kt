package action

import helper.DartNameUtils
import helper.GenerationMode
import helper.GenerationOptions
import java.nio.charset.StandardCharsets

data class GeneratedFile(
    val relativePath: String,
    val content: String,
)

class GetXTemplateGenerator(
    private val templateLoader: (String) -> String = ::loadTemplate,
) {
    fun generate(moduleName: String, options: GenerationOptions): List<GeneratedFile> {
        val normalizedName = DartNameUtils.normalizeModuleName(moduleName)
            ?: throw IllegalArgumentException(
                "Module name must start with a letter and contain only letters, numbers, spaces, '_' or '-'.",
            )

        validateModuleSuffixes(options)

        val module = options.module
        val namePrefix = if (options.function.usePrefix) {
            "${DartNameUtils.toSnakeCase(normalizedName)}_"
        } else {
            ""
        }
        val folderPrefix = if (options.function.useFolder) {
            val suffix = if (options.setting.useFolderSuffix) {
                "_${DartNameUtils.lowercase(module.viewName)}"
            } else {
                ""
            }
            "${DartNameUtils.toSnakeCase(normalizedName)}$suffix/"
        } else {
            ""
        }

        val files = mutableListOf<GeneratedFile>()
        when (options.mode) {
            GenerationMode.DEFAULT -> {
                addFile(
                    files,
                    "state.dart",
                    folderPrefix,
                    "$namePrefix${DartNameUtils.lowercase(module.stateName)}.dart",
                    normalizedName,
                    namePrefix,
                    options,
                )
                addFile(
                    files,
                    "logic.dart",
                    folderPrefix,
                    "$namePrefix${DartNameUtils.lowercase(module.logicName)}.dart",
                    normalizedName,
                    namePrefix,
                    options,
                )
                addFile(
                    files,
                    "view.dart",
                    folderPrefix,
                    "$namePrefix${DartNameUtils.lowercase(module.viewFileName)}.dart",
                    normalizedName,
                    namePrefix,
                    options,
                )
            }

            GenerationMode.EASY -> {
                addFile(
                    files,
                    "easy/logic.dart",
                    folderPrefix,
                    "$namePrefix${DartNameUtils.lowercase(module.logicName)}.dart",
                    normalizedName,
                    namePrefix,
                    options,
                )
                addFile(
                    files,
                    "easy/view.dart",
                    folderPrefix,
                    "$namePrefix${DartNameUtils.lowercase(module.viewFileName)}.dart",
                    normalizedName,
                    namePrefix,
                    options,
                )
            }
        }

        if (options.function.addBinding) {
            val bindingTemplate = if (options.function.useGetX5) {
                "binding_5.dart"
            } else {
                "binding_4.dart"
            }
            addFile(
                files,
                bindingTemplate,
                folderPrefix,
                "${namePrefix}binding.dart",
                normalizedName,
                namePrefix,
                options,
            )
        }

        return files
    }

    private fun addFile(
        files: MutableList<GeneratedFile>,
        inputFileName: String,
        folderPrefix: String,
        outputFileName: String,
        moduleName: String,
        namePrefix: String,
        options: GenerationOptions,
    ) {
        files += GeneratedFile(
            relativePath = "$folderPrefix$outputFileName",
            content = render(inputFileName, moduleName, namePrefix, options),
        )
    }

    private fun render(
        inputFileName: String,
        moduleName: String,
        namePrefix: String,
        options: GenerationOptions,
    ): String {
        var content = templateLoader(templatePath(inputFileName, options))

        if (options.function.logicInBuild &&
            !options.function.isPageView &&
            inputFileName.endsWith("view.dart")
        ) {
            content = applyLogicInBuildTransformation(content)
        }

        if (options.function.useScaffold && inputFileName.endsWith("view.dart")) {
            content = applyUseScaffoldTransformation(content)
        }

        if (inputFileName.endsWith("view.dart")) {
            if (options.function.addBinding) {
                content = content.replace(
                    "Get.put(@nameLogic())",
                    "Get.find<@nameLogic>()",
                )
            }
            if (options.function.useGetX5) {
                content = content
                    .replace("Get.find", "Bind.find")
                    .replace("Get.delete", "Bind.delete")
            }
        }

        val module = options.module
        val replacements = mapOf(
            "logic.dart" to "$namePrefix${DartNameUtils.lowercase(module.logicName)}.dart",
            "state.dart" to "$namePrefix${DartNameUtils.lowercase(module.stateName)}.dart",
            "@namePage" to "$moduleName${module.viewName}",
            "@nameLogic" to "$moduleName${module.logicName}",
            "@nameState" to "$moduleName${module.stateName}",
            "@nameBinding" to "$moduleName" + "Binding",
            "@name" to moduleName,
        )

        return replacements.entries
            .sortedByDescending { it.key.length }
            .fold(content) { current, (key, value) -> current.replace(key, value) }
    }

    private fun templatePath(inputFileName: String, options: GenerationOptions): String {
        val folder = when {
            inputFileName.endsWith("view.dart") && options.function.isPageView -> "pageView"
            inputFileName.endsWith("view.dart") && options.function.autoDispose -> "auto"
            inputFileName.endsWith("logic.dart") && options.function.addLifecycle -> "lifecycle"
            else -> "normal"
        }
        return "/templates/$folder/$inputFileName"
    }

    private fun applyLogicInBuildTransformation(content: String): String {
        val twoFieldPattern = Regex(
            "  final @nameLogic \\w+ = Get\\.put\\(@nameLogic\\(\\)\\);\\r?\\n" +
                "  final @nameState \\w+ = Get\\.find<@nameLogic>\\(\\)\\.\\w+;\\r?\\n" +
                "\\r?\\n" +
                "  @override\\r?\\n" +
                "  Widget build\\(BuildContext context\\) \\{\\r?\\n" +
                "    return",
        )
        val twoFieldReplacement =
            "  @override\n" +
                "  Widget build(BuildContext context) {\n" +
                "    final @nameLogic logic = Get.put(@nameLogic());\n" +
                "    final @nameState state = Get.find<@nameLogic>().state;\n" +
                "\n" +
                "    return"

        val oneFieldPattern = Regex(
            "  final @nameLogic \\w+ = Get\\.put\\(@nameLogic\\(\\)\\);\\r?\\n" +
                "\\r?\\n" +
                "  @override\\r?\\n" +
                "  Widget build\\(BuildContext context\\) \\{\\r?\\n" +
                "    return",
        )
        val oneFieldReplacement =
            "  @override\n" +
                "  Widget build(BuildContext context) {\n" +
                "    final @nameLogic logic = Get.put(@nameLogic());\n" +
                "\n" +
                "    return"

        return content
            .replace(twoFieldPattern, twoFieldReplacement)
            .replace(oneFieldPattern, oneFieldReplacement)
    }

    private fun applyUseScaffoldTransformation(content: String): String {
        return content.replace(
            "    return Container();",
            "    return Scaffold(\n" +
                "      appBar: AppBar(\n" +
                "        title: const Text('@namePage'),\n" +
                "      ),\n" +
                "      body: const Center(\n" +
                "        child: Text('@namePage'),\n" +
                "      ),\n" +
                "    );",
        )
    }

    private fun validateModuleSuffixes(options: GenerationOptions) {
        val invalidName = listOf(
            "view name" to options.module.viewName,
            "view file name" to options.module.viewFileName,
            "logic name" to options.module.logicName,
            "state name" to options.module.stateName,
        ).firstOrNull { (_, value) -> !DartNameUtils.isValidIdentifier(value) }

        if (invalidName != null) {
            throw IllegalArgumentException(
                "Configured ${invalidName.first} must be a Dart identifier starting with a letter.",
            )
        }
    }
}

private fun loadTemplate(path: String): String {
    return GetXTemplateGenerator::class.java.getResourceAsStream(path)?.use { input ->
        input.readBytes().toString(StandardCharsets.UTF_8)
    } ?: throw IllegalStateException("Template resource not found: $path")
}
