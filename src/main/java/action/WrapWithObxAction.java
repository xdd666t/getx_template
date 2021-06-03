package action;

import org.jetbrains.annotations.NotNull;

public class WrapWithObxAction extends WrapWithAction {
    public WrapWithObxAction() {
        super(SnippetType.Obx);
    }

    @NotNull
    public String getText() {
        return "Wrap with GetX-Obx";
    }
}
