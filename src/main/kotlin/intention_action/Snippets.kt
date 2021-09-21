package intention_action

import helper.DataService.Companion.instance
import java.util.*

object Snippets {
    private val data = instance
    const val PREFIX_SELECTION = "Subject"
    private val SUFFIX1 = data.module.logicName
    val GetX_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1

    fun getSnippet(snippetType: SnippetType?, widget: String): String {
        return when (snippetType) {
            SnippetType.Obx -> snippetObx(widget)
            SnippetType.GetBuilder -> snippetGetBuilder(widget)
            SnippetType.GetBuilderAutoDispose -> snippetGetBuilderAutoDispose(widget)
            SnippetType.GetX -> snippetGetX(widget)
            else -> ""
        }
    }

    private fun snippetObx(widget: String): String {
        return String.format(
            """Obx(() {
  return %1${"$"}s;
})""", widget
        )
    }

    private fun snippetGetBuilder(widget: String): String {
        return String.format(
            """GetBuilder<%1${"$"}s>(builder: (%2${"$"}s) {
  return %3${"$"}s;
})""", GetX_SNIPPET_KEY, data.module.logicName.lowercase(Locale.getDefault()), widget
        )
    }

    private fun snippetGetBuilderAutoDispose(widget: String): String {
        return String.format(
            """GetBuilder<%1${"$"}s>(
  assignId: true,
  builder: (%2${"$"}s) {
    return %3${"$"}s;
  },
)""", GetX_SNIPPET_KEY, data.module.logicName.lowercase(Locale.getDefault()), widget
        )
    }

    private fun snippetGetX(widget: String): String {
        return String.format(
            """GetX<%1${"$"}s>(
  init: %1${"$"}s(),
  initState: (_) {},
  builder: (%2${"$"}s) {
    return %3${"$"}s;
  },
)""", GetX_SNIPPET_KEY, data.module.logicName.lowercase(Locale.getDefault()), widget
        )
    }
}