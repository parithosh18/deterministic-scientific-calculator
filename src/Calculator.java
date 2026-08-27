import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ExpressionParser parser = new ExpressionParser();

        List<String> history = new ArrayList<>();

        System.out.println("==========================================");
        System.out.println("   DETERMINISTIC SCIENTIFIC CALCULATOR");
        System.out.println("==========================================");
        System.out.println("Type 'help' to see supported operations.");
        System.out.println("Type 'history' to see previous calculations.");
        System.out.println("Type 'clear' to clear history.");
        System.out.println("Type 'exit' to quit.");
        System.out.println();

        while (true) {

            System.out.print("Enter expression: ");
            String input = scanner.nextLine().trim();

            // Exit command
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Calculator closed.");
                break;
            }

            // Help command
            if (input.equalsIgnoreCase("help")) {
                showHelp();
                continue;
            }

            // History command
            if (input.equalsIgnoreCase("history")) {

                if (history.isEmpty()) {
                    System.out.println("No calculation history.");
                } else {

                    System.out.println("\n--- Calculation History ---");

                    for (int i = 0; i < history.size(); i++) {
                        System.out.println(
                                (i + 1) + ". " + history.get(i)
                        );
                    }

                    System.out.println("----------------------------");
                }

                continue;
            }

            // Clear history command
            if (input.equalsIgnoreCase("clear")) {

                history.clear();

                System.out.println("Calculation history cleared.");

                continue;
            }

            // Empty input
            if (input.isEmpty()) {
                System.out.println(
                        "Error: Expression cannot be empty."
                );
                continue;
            }

            try {

                double result = parser.evaluate(input);

                String formattedResult = formatResult(result);

                history.add(
                        input + " = " + formattedResult
                );

                System.out.println(
                        "Result: " + formattedResult
                );

            } catch (CalculatorException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }

            System.out.println();
        }

        scanner.close();
    }

    // =========================================================
    // FORMAT RESULT
    // =========================================================

    private static String formatResult(double result) {

        if (result == Math.rint(result)) {
            return String.format("%.0f", result);
        }

        if (Math.abs(result) >= 1e10
                || (Math.abs(result) > 0
                && Math.abs(result) < 1e-6)) {

            return String.format("%.10e", result);
        }

        return String.format("%.10f", result)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    // =========================================================
    // HELP MENU
    // =========================================================

    private static void showHelp() {

        System.out.println();
        System.out.println("========== SUPPORTED OPERATIONS ==========");

        System.out.println("\nArithmetic:");
        System.out.println("+     Addition");
        System.out.println("-     Subtraction");
        System.out.println("*     Multiplication");
        System.out.println("/     Division");
        System.out.println("%     Modulus");
        System.out.println("^     Power");
        System.out.println("!     Factorial");

        System.out.println("\nTrigonometric:");
        System.out.println("sin(x)");
        System.out.println("cos(x)");
        System.out.println("tan(x)");
        System.out.println("asin(x)");
        System.out.println("acos(x)");
        System.out.println("atan(x)");

        System.out.println("\nHyperbolic:");
        System.out.println("sinh(x)");
        System.out.println("cosh(x)");
        System.out.println("tanh(x)");

        System.out.println("\nLogarithmic:");
        System.out.println("log(x)  - Base 10 logarithm");
        System.out.println("ln(x)   - Natural logarithm");

        System.out.println("\nOther Functions:");
        System.out.println("sqrt(x)");
        System.out.println("cbrt(x)");
        System.out.println("abs(x)");
        System.out.println("exp(x)");
        System.out.println("floor(x)");
        System.out.println("round(x)");

        System.out.println("\nTwo-Argument Functions:");
        System.out.println("pow(x,y)");
        System.out.println("max(x,y)");
        System.out.println("min(x,y)");

        System.out.println("\nConstants:");
        System.out.println("pi");
        System.out.println("e");

        System.out.println("\nCommands:");
        System.out.println("help");
        System.out.println("history");
        System.out.println("clear");
        System.out.println("exit");

        System.out.println("==========================================");
        System.out.println();
    }
}