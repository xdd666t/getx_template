package intention_action

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager

abstract class WrapWithAction(private val snippetType: SnippetType) :
    PsiElementBaseIntentionAction(),
    IntentionAction {

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (editor == null) {
            return false
        }

        val currentFile = getCurrentFile(project, editor)
        if (currentFile?.virtualFile?.extension?.equals("dart", ignoreCase = true) != true) {
            return false
        }

        return WrapHelper.callExpressionFinder(element) != null
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val callExpression = WrapHelper.callExpressionFinder(element) ?: return
        try {
            WriteCommandAction.runWriteCommandAction(project) {
                invokeSnippetAction(project, editor, callExpression)
            }
        } catch (exception: Exception) {
            LOGGER.warn("Unable to wrap Dart expression with $snippetType.", exception)
        }
    }

    private fun invokeSnippetAction(project: Project, editor: Editor, element: PsiElement) {
        val document = editor.document
        val elementRange = element.textRange
        val offsetStart = elementRange.startOffset
        val offsetEnd = elementRange.endOffset
        if (!WrapHelper.isSelectionValid(offsetStart, offsetEnd) ||
            offsetEnd > document.textLength
        ) {
            return
        }

        val selectedText = document.getText(TextRange.create(offsetStart, offsetEnd))
        val replacement = Snippets.getSnippet(snippetType, selectedText)
        if (replacement.isEmpty()) {
            return
        }

        document.replaceString(offsetStart, offsetEnd, replacement)
        updateCarets(editor, offsetStart, replacement)

        val documentManager = PsiDocumentManager.getInstance(project)
        documentManager.commitDocument(document)
        val currentFile = getCurrentFile(project, editor)
        if (currentFile != null) {
            CodeStyleManager.getInstance(project).reformatText(
                currentFile,
                offsetStart,
                offsetStart + replacement.length,
            )
        }
    }

    private fun updateCarets(editor: Editor, offsetStart: Int, replacement: String) {
        val caretModel = editor.caretModel
        caretModel.removeSecondaryCarets()

        val key = Snippets.getSnippetKey()
        val keyOffset = replacement.indexOf(key)
        if (keyOffset < 0) {
            caretModel.primaryCaret.moveToOffset(offsetStart + replacement.length)
            return
        }

        val initialCaret = caretModel.primaryCaret
        val snippetCaret = caretModel.addCaret(
            editor.offsetToVisualPosition(offsetStart + keyOffset),
        )
        if (snippetCaret == null) {
            initialCaret.moveToOffset(offsetStart + keyOffset)
            initialCaret.setSelection(
                offsetStart + keyOffset,
                offsetStart + keyOffset + Snippets.PREFIX_SELECTION.length,
            )
            return
        }

        snippetCaret.setSelection(
            offsetStart + keyOffset,
            offsetStart + keyOffset + Snippets.PREFIX_SELECTION.length,
        )
        if (initialCaret !== snippetCaret && !initialCaret.hasSelection()) {
            caretModel.removeCaret(initialCaret)
        }
    }

    override fun startInWriteAction(): Boolean = true

    private fun getCurrentFile(project: Project, editor: Editor): PsiFile? {
        return PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
    }

    private companion object {
        val LOGGER: Logger = Logger.getInstance(WrapWithAction::class.java)
    }
}
