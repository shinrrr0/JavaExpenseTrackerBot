package bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;
import java.util.function.Consumer;

public class PengradGateway implements TelegramGateway {

    private final TelegramBot bot;

    public PengradGateway(String token) {
        bot = new TelegramBot(token);
    }

    @Override
    public void start(Consumer<Input> handler) {
        bot.setUpdatesListener(updates -> processUpdates(updates, handler));
    }

    private int processUpdates(List<Update> updates, Consumer<Input> handler) {
        for (Update update : updates) {
            processUpdate(update, handler);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update, Consumer<Input> handler) {
        if (update.message() == null || update.message().text() == null) {return;}

        long chatId = update.message().chat().id();
        String text = update.message().text();

        Input input = Input.fromText(chatId, text);

        handler.accept(input);
    }

    @Override
    public void sendMessage(long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }
}