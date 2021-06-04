package intention_action;

import org.jetbrains.annotations.NotNull;

public class WrapWithGetXAction extends WrapWithAction {
    public WrapWithGetXAction() {
        super(SnippetType.GetX);
    }

    @NotNull
    public String getText() {
        return "Wrap with GetX";
    }
}
