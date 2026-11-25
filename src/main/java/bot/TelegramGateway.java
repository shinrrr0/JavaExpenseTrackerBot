package bot;

import java.util.function.Consumer;

public interface TelegramGateway {

    void start(Consumer<Input> handler);
    void sendMessage(long chatId, String text);
}