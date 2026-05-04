package intention_action

import helper.DataService
import java.util.*

object Snippets {
    const val PREFIX_SELECTION = "Subject"

    fun getSnippetKey(): String {
        val data = DataService.instance
        return PREFIX_SELECTION + data.module.logicName
    }

    fun getSnippet(snippetType: SnippetType?, widget: String): String {
        return when (snippetType) {
            SnippetType.Obx -> snippetObx(widget)
            SnippetType.GetBuilder -> snippetGetBuilder(widget)
            SnippetType.Observer -> snippetObserver(widget)
            SnippetType.GetBuilderAutoDispose -> snippetGetBuilderAutoDispose(widget)
            SnippetType.GetX -> snippetGetX(widget)
            else -> ""
        }
    }

    private fun logicName(): String {
        return DataService.instance.module.logicName
    }

    private fun snippetObx(widget: String): String {
        return String.format(
            """Obx(() {
  return %1${"$"}s;
})""", widget
        )
    }

    private fun snippetGetBuilder(widget: String): String {
        val key = getSnippetKey()
        return String.format(
            """GetBuilder<%1${"$"}s>(builder: (%2${"$"}s) {
  return %3${"$"}s;
})""", key, logicName().lowercase(Locale.getDefault()), widget
        )
    }

    private fun snippetObserver(widget: String): String {
        return String.format(
            """Observer(builder: (BuildContext context) {
  return %1${"$"}s;
})""", widget
        )
    }

    private fun snippetGetBuilderAutoDispose(widget: String): String {
        val key = getSnippetKey()
        return String.format(
            """GetBuilder<%1${"$"}s>(
  assignId: true,
  builder: (%2${"$"}s) {
    return %3${"$"}s;
  },
)""", key, logicName().lowercase(Locale.getDefault()), widget
        )
    }

    private fun snippetGetX(widget: String): String {
        val key = getSnippetKey()
        return String.format(
            """GetX<%1${"$"}s>(
  init: %1${"$"}s(),
  initState: (_) {},
  builder: (%2${"$"}s) {
    return %3${"$"}s;
  },
)""", key, logicName().lowercase(Locale.getDefault()), widget
        )
    }
}