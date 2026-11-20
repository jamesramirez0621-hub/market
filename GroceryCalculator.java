import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GroceryCalculator {
    private static final List<Product> PRODUCTS = Arrays.asList(
            new Product("Milk", new BigDecimal("3.50")),
            new Product("Bread", new BigDecimal("4.25")));

    private static class Product {
        private final String name;
        private final BigDecimal unitPrice;

        Product(String name, BigDecimal unitPrice) {
            this.name = name;
            this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        }
    }

    private static class GroceryItem {
        private final String name;
        private final int quantity;
        private final BigDecimal unitPrice;

        GroceryItem(String name, int quantity, BigDecimal unitPrice) {
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal getTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<GroceryItem> items = new ArrayList<>();

        System.out.println("Grocery Calculator");
        printProductList();
        System.out.println("Choose an item number or name, or type 'done' to finish.");

        while (true) {
            System.out.print("\nYour choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("done")) {
                break;
            }

            int productIndex = findProductIndex(choice);
            if (productIndex == -1) {
                System.out.println("Enter an item number or name from the list, or 'done'.");
                continue;
            }

            Product product = PRODUCTS.get(productIndex);
            int quantity = readPositiveInt(scanner, "Quantity: ");
            items.add(new GroceryItem(product.name, quantity, product.unitPrice));
            System.out.printf("Added: %d x %s @ %s%n",
                    quantity, product.name, formatMoney(product.unitPrice));
        }

        printReceipt(items);
        scanner.close();
    }

    private static int findProductIndex(String choice) {
        for (int i = 0; i < PRODUCTS.size(); i++) {
            if (PRODUCTS.get(i).name.equalsIgnoreCase(choice)) {
                return i;
            }
        }

        try {
            int productIndex = Integer.parseInt(choice) - 1;
            if (productIndex >= 0 && productIndex < PRODUCTS.size()) {
                return productIndex;
            }
        } catch (NumberFormatException exception) {
            return -1;
        }
        return -1;
    }

    private static void printProductList() {
        System.out.println("\nAvailable items:");
        for (int i = 0; i < PRODUCTS.size(); i++) {
            Product product = PRODUCTS.get(i);
            System.out.printf("%d. %-20s %s each%n",
                    i + 1, product.name, formatMoney(product.unitPrice));
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Quantity must be greater than zero.");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static void printReceipt(List<GroceryItem> items) {
        System.out.println("\n---------------- Grocery Receipt ----------------");
        if (items.isEmpty()) {
            System.out.println("No items entered.");
        } else {
            BigDecimal total = BigDecimal.ZERO.setScale(2);
            for (GroceryItem item : items) {
                BigDecimal itemTotal = item.getTotal();
                total = total.add(itemTotal);
                System.out.printf("%-20s %4d x %8s = %10s%n",
                        item.name,
                        item.quantity,
                        formatMoney(item.unitPrice),
                        formatMoney(itemTotal));
            }
            System.out.println("------------------------------------------------");
            System.out.printf("TOTAL: %38s%n", formatMoney(total));
        }
        System.out.println("------------------------------------------------");
    }

    private static String formatMoney(BigDecimal amount) {
        return "$" + amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
