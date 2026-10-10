package bot;

import java.time.LocalDateTime;

public record Expense(long id, long amount, ExpenseCategory category, String description, LocalDateTime dateTime) {
    public String getFormattedAmount() {
        return String.format("%d.%02d ₽", amount/100, amount%100);
    }
}
