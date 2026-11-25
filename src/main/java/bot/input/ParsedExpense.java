package bot.input;

import bot.model.ExpenseCategory;

public record ParsedExpense(
        long amount,
        ExpenseCategory category,
        String description
) {}