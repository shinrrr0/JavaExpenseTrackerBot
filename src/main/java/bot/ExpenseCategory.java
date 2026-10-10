package bot;

public enum ExpenseCategory {
    FOOD("еда", "🍔"),
    TRANSPORT("транспорт", "\uD83D\uDE8C"),
    ENTERTAINMENT("развлечения", "\uD83C\uDFAE"),
    SHOPPING("покупки", "\uD83D\uDECD\uFE0F"),
    EDUCATION("учёба", "\uD83D\uDCDA"),
    HEALTH("здоровье", "💊"),
    OTHER("другое", "\uD83D\uDCE6");

    private final String title;
    private final String emoji;

    ExpenseCategory(String title, String emoji) {
        this.title = title;
        this.emoji = emoji;
    }

    public String getTitle() {
        return title;
    }

    public String getEmoji() {
        return emoji;
    }

    public static ExpenseCategory fromString(String input) {
        if (input == null) {
            return null;
        }

        String normalized = input.trim();

        for (ExpenseCategory category : values()) {
            if (category.title.equalsIgnoreCase(normalized)) {
                return category;
            }
        }

        return null;
    }

    public static ExpenseCategory fromValue(String input) {
        if (input == null) {
            return null;
        }

        String normalized = input.trim();

        for (ExpenseCategory category : values()) {
            if (category.title.equalsIgnoreCase(normalized)) {
                return category;
            }
        }

        return null;
    }
}
