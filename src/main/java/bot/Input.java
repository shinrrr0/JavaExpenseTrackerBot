package bot;

public abstract class Input {

    private final long chatId;
    private final String text;

    protected Input(long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public long chatId() {
        return chatId;
    }

    public String text() {
        return text;
    }

    public static Input fromText(long chatId, String text) {
        if (text.startsWith("/")) {
            return new CommandInput(chatId, text);
        }
        else {
            return new ExpenseInput(chatId, text);
        }
    }
}