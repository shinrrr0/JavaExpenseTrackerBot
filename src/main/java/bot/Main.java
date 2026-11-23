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


// интерфейсы для работы с файлами
// прослойка между библиотекой и ботом
// абстрактный класс input
// валидация expense на уровне класса
// '/' проверка, что ктаегроия не начинается со слэша
// комментарии к методам интерфейса, рекордам
//