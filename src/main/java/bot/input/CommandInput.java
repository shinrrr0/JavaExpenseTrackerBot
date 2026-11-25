package bot.input;

public class CommandInput extends Input {

    public CommandInput(long chatId, String text) {
        super(chatId, text);
    }

    public String command() {
        String[] parts = text().split(" ", 2);
        return parts[0];
    }
}