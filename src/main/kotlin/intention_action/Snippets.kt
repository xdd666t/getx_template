package intention_action

import helper.DataService
import java.util.Locale

object Snippets {
    const val PREFIX_SELECTION = "Subject"

    fun getSnippetKey(): String {
        return PREFIX_SELECTION + DataService.instance.module.logicName
    }

    fun getSnippet(snippetType: SnippetType, widget: String): String {
        return when (snippetType) {
            SnippetType.Obx -> snippetObx(widget)
            SnippetType.GetBuilder -> snippetGetBuilder(widget)
            SnippetType.Observer -> snippetObserver(widget)
            SnippetType.GetBuilderAutoDispose -> snippetGetBuilderAutoDispose(widget)
            SnippetType.GetX -> snippetGetX(widget)
        }
    }

    private fun logicName(): String {
        return DataService.instance.module.logicName
    }

    private fun logicVariableName(): String {
        return logicName().lowercase(Locale.ROOT)
    }

    private fun snippetObx(widget: String): String = """Obx(() {
  return $widget;
})"""

    private fun snippetGetBuilder(widget: String): String {
        return """GetBuilder<${getSnippetKey()}>(builder: (${logicVariableName()}) {
  return $widget;
})"""
    }

    private fun snippetObserver(widget: String): String = """Observer(builder: (BuildContext context) {
  return $widget;
})"""

    private fun snippetGetBuilderAutoDispose(widget: String): String {
        return """GetBuilder<${getSnippetKey()}>(
  assignId: true,
  builder: (${logicVariableName()}) {
    return $widget;
  },
)"""
    }

    private fun snippetGetX(widget: String): String {
        return """GetX<${getSnippetKey()}>(
  init: ${getSnippetKey()}(),
  initState: (_) {},
  builder: (${logicVariableName()}) {
    return $widget;
  },
)"""
    }
}
