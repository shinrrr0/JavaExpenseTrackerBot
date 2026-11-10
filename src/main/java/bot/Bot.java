package bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.io.IOException;

import java.util.List;
//import java.util.Random;

public class Bot {
    private static final String HELP_TEXT = """
Привет!
Это справка для того, чтобы ты разобрался, что умеет бот.
1./start - начало работы бота
2./categories - напишет какие категории доступны для ввода
3./history - покажет 10 последних твоих вводов трат и категорий
Для того, чтобы ты не ошибся, держи корректный ввод сообщения, чтобы бот добавил его в список: 
Потраченная сумма, категория, комментарий(не обязателен). 
Пример: 250 транспорт такси """;
    private final TelegramBot bot;
    private final ExpenseParser parser;
    private final FileStorage storage;
    private final ExpenseService service;

    public Bot(String token) {
        bot = new TelegramBot(token);
        parser = new ExpenseParser();

        try {
            storage = new FileStorage();
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось создать каталог для хранения расходов", e);
        }

        service = new ExpenseService(storage);
    }

    public void start() {
        bot.setUpdatesListener(this::processUpdates);
    }

    private int processUpdates(List<Update> updates) {
        for (Update update : updates) {
            processUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        if (update.message() == null || update.message().text() == null) {return;}

        long chatId = update.message().chat().id();
        String text = update.message().text();

        String answer = "";
        if (text.startsWith("/")) {
            answer = processCommand(chatId,text);
        }
        else {
            try {
                ParsedExpense parsed = parser.parse(text);
                Expense expense = service.addExpense(chatId, parsed.amount(), parsed.category(), parsed.description());
                answer = "✅ Расход #%d добавлен!\n%s %s — %s\n%s".formatted(
                        expense.id(),
                        expense.category().getEmoji(),
                        expense.category().getTitle(),
                        expense.getFormattedAmount(),
                        expense.description()
                );
            } catch (IllegalArgumentException e) {
                answer = "Ошибка: " + e.getMessage();
            } catch (IOException e) {
                answer = "Не удалось сохранить расход. Попробуйте позже.";
                e.printStackTrace();
            }
        }

        bot.execute(new SendMessage(chatId, answer));
    }

    private String processCommand(long chatId,String text) {
        String[] parts = text.split(" ", 2);
        String command = parts[0];

        return switch (command) {
            case "/start" -> commandStart();
            case "/help" -> commandHelp();
            case "/categories" -> commandCategories();
            case "/history" -> commandHistory(chatId);
            default -> "Ты написал: " + text;
        };
    }

    private String commandStart() {
        return "Привет! Этот бот поможет тебе учитывать свои расходы\nВведи /help чтобы увидеть список доступных комманд";
    }

    private String commandCategories() {
        StringBuilder result = new StringBuilder();

        for (ExpenseCategory category : ExpenseCategory.values()) {
            result.append(category.getEmoji())
                    .append(" ")
                    .append(category.getTitle())
                    .append("\n");
        }

        return result.toString();
    }

    private String commandHistory(long chatId) {

        try {
            List<Expense> expenses = service.getExpenses(chatId);
            if (expenses.isEmpty()) return "История расходов пуста";

            StringBuilder sb = new StringBuilder();
            for (int i = expenses.size() - 1;i >= 0 && i >= expenses.size() - 10;i--) {
                Expense expense = expenses.get(i);
                sb.append("#%d %s %s — %s\n".formatted(
                        expense.id(), expense.category().getEmoji(), expense.category().getTitle(), expense.getFormattedAmount()));
                if (!expense.description().isBlank()) sb.append(expense.description()).append("\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Не удалось загрузить историю";
        }
    }

    private String commandHelp() {
        return HELP_TEXT;
    }
}
