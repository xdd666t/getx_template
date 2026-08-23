package intention_action;

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

object WrapHelper {
    @JvmStatic
    fun callExpressionFinder(psiElement: PsiElement): PsiElement? {
        var current: PsiElement? = psiElement.parent
        while (current != null && current !is PsiFile) {
            val elementType = (current.node?.elementType?.toString()
                ?: current.toString()).substringAfterLast(':')
            if (elementType == "CALL_EXPRESSION" || elementType == "ARGUMENTS") {
                return current.takeIf {
                    psiElement.text.isNotEmpty() && it.text.startsWith(psiElement.text)
                }
            }
            current = current.parent
        }
        return null
    }

    @JvmStatic
    fun isSelectionValid(start: Int, end: Int): Boolean {
        if (start <= -1 || end <= -1) {
            return false
        }

        if (start >= end) {
            return false
        }

        return true
    }
}
