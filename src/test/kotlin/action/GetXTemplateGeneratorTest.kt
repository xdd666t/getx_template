package action

import helper.FunctionInfo
import helper.GenerationMode
import helper.GenerationOptions
import helper.ModuleNameSuffix
import helper.SettingInfo
import helper.TemplateKind
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class GetXTemplateGeneratorTest {
    private val baseOptions = GenerationOptions(
        mode = GenerationMode.DEFAULT,
        function = FunctionInfo(
            useGetX5 = false,
            useFolder = true,
            usePrefix = false,
            isPageView = false,
            autoDispose = false,
            addLifecycle = false,
            addBinding = false,
            logicInBuild = false,
            useScaffold = false,
        ),
        module = ModuleNameSuffix(),
        setting = SettingInfo(),
        templateKind = TemplateKind.PAGE,
    )

    @Test
    fun generatesDefaultFilesWithResolvedClassNames() {
        val files = GetXTemplateGenerator().generate("user_profile", baseOptions)

        assertEquals(
            listOf(
                "user_profile/state.dart",
                "user_profile/logic.dart",
                "user_profile/view.dart",
            ),
            files.map(GeneratedFile::relativePath),
        )
        assertContains(files.first().content, "class UserProfileState")
        assertContains(files.last().content, "class UserProfilePage")
        assertFalse(files.any { it.content.contains("@name") })
    }

    @Test
    fun appliesPrefixScaffoldAndBindingOptions() {
        val options = baseOptions.copy(
            function = baseOptions.function.copy(
                usePrefix = true,
                addBinding = true,
                useScaffold = true,
            ),
        )

        val files = GetXTemplateGenerator().generate("home_page", options)
        assertEquals(4, files.size)
        assertContains(files.last().content, "class HomePageBinding")
        assertContains(files.last().content, "Get.lazyPut")
        assertContains(files.first { it.relativePath.endsWith("view.dart") }.content, "return Scaffold(")
    }

    @Test
    fun rejectsInvalidConfiguredSuffix() {
        val options = baseOptions.copy(
            module = baseOptions.module.copy(logicName = "logic-name"),
        )

        assertFailsWith<IllegalArgumentException> {
            GetXTemplateGenerator().generate("Home", options)
        }
    }
}
