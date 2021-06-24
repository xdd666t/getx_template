package live_templates

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile

class GetXContext : TemplateContextType("FLUTTER", "Flutter") {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return file.name.endsWith(".dart")
    }
}