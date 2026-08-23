package live_templates

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.codeInsight.template.TemplateActionContext

@Suppress("DEPRECATION")
class GetXContext : TemplateContextType("FLUTTER", "Flutter", null) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.name.endsWith(".dart")
    }
}
