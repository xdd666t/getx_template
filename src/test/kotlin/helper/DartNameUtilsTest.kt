package helper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DartNameUtilsTest {
    @Test
    fun normalizesCommonModuleNameForms() {
        assertEquals("HomePage", DartNameUtils.normalizeModuleName("home_page"))
        assertEquals("HomePage", DartNameUtils.normalizeModuleName("home-page"))
        assertEquals("HomePage", DartNameUtils.normalizeModuleName("HomePage"))
    }

    @Test
    fun rejectsPathAndPunctuationInput() {
        assertNull(DartNameUtils.normalizeModuleName("../home"))
        assertNull(DartNameUtils.normalizeModuleName("home.page"))
        assertTrue(DartNameUtils.isValidIdentifier("CustomLogic"))
    }

    @Test
    fun convertsClassNameToSnakeCaseUsingRootLocale() {
        assertEquals("http_server", DartNameUtils.toSnakeCase("HTTPServer"))
        assertEquals("home_page", DartNameUtils.toSnakeCase("HomePage"))
    }
}
