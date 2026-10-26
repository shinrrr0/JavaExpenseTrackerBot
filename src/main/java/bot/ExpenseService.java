package bot;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class ExpenseService {

    private final FileStorage storage;

    public ExpenseService(FileStorage storage) {
        this.storage = storage;
    }

    public Expense addExpense(long chatId, long amount, ExpenseCategory category, String description) throws IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма расхода должна быть положительной");
        }
        if (category == null) {
            throw new IllegalArgumentException("Неизвестная категория");
        }
        if (description == null ||
                description.contains(";") ||
                description.contains("\n") ||
                description.contains("\r")) {
            throw new IllegalArgumentException("Некорректное описание");
        }

        List<Expense> expenses = storage.loadExpenses(chatId);

        long max_id = 0;

        for (Expense expense : expenses) {
            if (expense.id() > max_id) {
                max_id = expense.id();
            }
        }

        long new_id = max_id + 1;

        Expense expense = new Expense(new_id, amount, category, description, LocalDateTime.now());
        expenses.add(expense);

        storage.saveExpenses(chatId, expenses);

        return expense;
    }

    public List<Expense> getExpenses(long chatId) throws IOException {
        return storage.loadExpenses(chatId);
    }

    public long getTotalAmount(long chatId) throws IOException {
        List<Expense> expenses = getExpenses(chatId);
        long total_amount = 0;

        for (Expense expense: expenses) {
            total_amount += expense.amount();
        }

        return total_amount;
    }


}