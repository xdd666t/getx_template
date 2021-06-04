package intention_action;

import org.jetbrains.annotations.NotNull;

public class WrapWithGetBuilderAction extends WrapWithAction {
    public WrapWithGetBuilderAction() {
        super(SnippetType.GetBuilder);
    }

    @NotNull
    public String getText() {
        return "Wrap with GetBuilder";
    }
}
