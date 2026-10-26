package bot;

import java.math.BigDecimal;

public class ExpenseParser {
    public ParsedExpense parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Пустое сообщение");
        }

        String[] components = text.trim().split("\\s+", 3);

        if (components.length < 2) {
            throw new IllegalArgumentException("Неполный набор аргументов");
        }

        String s_amount = components[0];
        String s_category = components[1];

        ExpenseCategory category = ExpenseCategory.fromString(s_category);

        if (category == null) {
            throw new IllegalArgumentException("Неизвестная категория");
        }

        String s_description = "";

        if (components.length == 3) {
            s_description = components[2];
        }

        long amount;

        try {
            BigDecimal value = new BigDecimal(s_amount);
            amount = value.movePointRight(2).longValueExact();
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("Некорректная сумма расхода", e);
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма расхода должна быть положительной");
        }

        return new ParsedExpense(amount, category, s_description);
    }
}
