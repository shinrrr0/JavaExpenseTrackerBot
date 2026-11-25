package bot.service;

import bot.model.Expense;

import java.io.IOException;
import java.util.List;

public interface ExpenseStorage {
    List<Expense> loadExpenses(long chatId) throws IOException;

    void saveExpenses(long chatId, List<Expense> expenses) throws IOException;
}