package bot;

public record ParsedExpense(
        long amount,
        ExpenseCategory category,
        String description
) {}