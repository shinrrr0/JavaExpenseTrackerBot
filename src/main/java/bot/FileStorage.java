package bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class FileStorage {

    private final Path dataDirectory = Path.of("data");

    public FileStorage() throws IOException {
        Files.createDirectories(dataDirectory);
    }

    public Path getUserFile(long chatId) {
        String filename = chatId + ".txt";
        return dataDirectory.resolve(filename);
    }

    public boolean hasUserFile(long chatId) {
        Path filename = getUserFile(chatId);
        return Files.exists(filename);
    }

    private String serialize(Expense expense) {
        return String.format(
                "%s;%s;%s;%s;%s",
                expense.id(),
                expense.amount(),
                expense.category(),
                expense.description(),
                expense.dateTime()
        );
    }

    private Expense deserialize(String line) {
        String[] parts = line.split(";", 5);
        return new Expense(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1]),
                ExpenseCategory.valueOf(parts[2]),
                parts[3],
                LocalDateTime.parse(parts[4])
        );
    }

    public void saveExpenses(long chatId, List<Expense> expenses) throws IOException {
        Path path = getUserFile(chatId);

        List<String> lines = new ArrayList<>();
        for (Expense expense: expenses) {
            lines.add(serialize(expense));
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public List<Expense> loadExpenses(long chatId) throws IOException {
        if (!hasUserFile(chatId)) {
            return new ArrayList<>();
        }

        Path path = getUserFile(chatId);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Expense> expenses = new ArrayList<>();

        for (String line: lines) {
            expenses.add(deserialize(line));
        }

        return expenses;
    }
}