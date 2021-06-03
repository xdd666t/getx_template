package action;

public class Snippets {
    public static final String PREFIX_SELECTION = "Subject";

    public static final String SUFFIX1 = "GetX";

    public static final String BLOC_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;
    public static final String STATE_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;
    public static final String REPOSITORY_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;


    static String getSnippet(SnippetType snippetType, String widget) {
        switch (snippetType) {
            case Obx:
                return getXObxSnippet(widget);
            default:
                return "";
        }
    }

    private static String getXObxSnippet(String widget) {
        return String.format("Obx(() {\n" +
                "  return %1$s\n" +
                "})\n", widget);
    }
}
