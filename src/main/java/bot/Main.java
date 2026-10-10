package bot;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Не задана переменная окружения BOT_TOKEN");
        }

        Bot bot = new Bot(token);
        bot.start();
    }
}
