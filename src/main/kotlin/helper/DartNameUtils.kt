package helper

import java.util.Locale

object DartNameUtils {
    private val moduleNamePattern = Regex("^[A-Za-z][A-Za-z0-9_ -]*$")
    private val identifierPattern = Regex("^[A-Za-z][A-Za-z0-9_]*$")

    /**
     * Converts the user-facing module name to a valid Dart class name.
     *
     * Supported input examples include `home`, `home_page`, `home-page`, and
     * `HomePage`. Path separators and punctuation are rejected deliberately so
     * the result can never escape the selected target directory.
     */
    fun normalizeModuleName(value: String): String? {
        val trimmed = value.trim()
        if (!moduleNamePattern.matches(trimmed)) {
            return null
        }

        val words = trimmed.split(Regex("[\\s_-]+"))
            .filter(String::isNotEmpty)
        if (words.isEmpty()) {
            return null
        }

        return words.joinToString(separator = "") { word ->
            word.substring(0, 1).uppercase(Locale.ROOT) + word.substring(1)
        }
    }

    fun isValidIdentifier(value: String): Boolean = identifierPattern.matches(value)

    fun toSnakeCase(value: String): String {
        return value
            .replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1_$2")
            .replace(Regex("([a-z0-9])([A-Z])"), "$1_$2")
            .replace(Regex("[^A-Za-z0-9]+"), "_")
            .trim('_')
            .lowercase(Locale.ROOT)
    }

    fun lowercase(value: String): String = value.lowercase(Locale.ROOT)
}
