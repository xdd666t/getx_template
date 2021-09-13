package intention_action

class WrapWithGetBuilderAction : WrapWithAction(SnippetType.GetBuilder) {
    override fun getText(): String {
        return "Wrap with GetBuilder"
    }
}

class WrapWithGetBuilderAutoDisposeAction : WrapWithAction(SnippetType.GetBuilderAutoDispose) {
    override fun getText(): String {
        return "Wrap with GetBuilder (Auto Dispose)"
    }
}

class WrapWithGetXAction : WrapWithAction(SnippetType.GetX) {
    override fun getText(): String {
        return "Wrap with GetX"
    }
}

class WrapWithObxAction : WrapWithAction(SnippetType.Obx) {
    override fun getText(): String {
        return "Wrap with Obx"
    }
}
