package bot;

import java.time.LocalDateTime;

public record Expense(long id, long amount, ExpenseCategory category, String description, LocalDateTime dateTime ) {
    public Expense {
        if (id <= 0) {
            throw new IllegalArgumentException("ID расхода должен быть положительным");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма расхода должна быть положительной");
        }

        if (category == null) {
            throw new IllegalArgumentException("Категория не указана");
        }

        if (description == null || description.contains(";") || description.contains("\n") || description.contains("\r")) {
            throw new IllegalArgumentException("Некорректное описание расхода");
        }

        if (dateTime == null) {
            throw new IllegalArgumentException("Дата расхода не указана");
        }
    }

    public String getFormattedAmount() {
        return String.format("%d.%02d ₽", amount / 100, amount % 100);
    }
}