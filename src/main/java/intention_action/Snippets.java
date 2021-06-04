package intention_action;

import helper.DataService;

public class Snippets {
    public static final DataService data = DataService.getInstance();

    public static final String PREFIX_SELECTION = "Subject";
    public static final String SUFFIX1 = data.logicName;

    public static final String GetX_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;
    public static final String STATE_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;


    static String getSnippet(SnippetType snippetType, String widget) {
        switch (snippetType) {
            case Obx:
                return snippetObx(widget);
            case GetBuilder:
                return snippetGetBuilder(widget);
            case GetX:
                return snippetGetX(widget);
            default:
                return "";
        }
    }

    private static String snippetObx(String widget) {
        return String.format("Obx(() {\n" +
                "  return %1$s;\n" +
                "})", widget);
    }

    private static String snippetGetBuilder(String widget) {
        return String.format("GetBuilder<%1$s>(\n" +
                "  builder: (%2$s) {\n" +
                "    return %3$s;\n" +
                "  },\n" +
                ")", GetX_SNIPPET_KEY, data.logicName.toLowerCase(), widget);
    }

    private static String snippetGetX(String widget) {
        return String.format("GetX<%1$s>(\n" +
                "  init: %1$s(),\n" +
                "  initState: (_) {},\n" +
                "  builder: (%2$s) {\n" +
                "    return %3$s;\n" +
                "  },\n" +
                ")", GetX_SNIPPET_KEY, data.logicName.toLowerCase(), widget);
    }
}
