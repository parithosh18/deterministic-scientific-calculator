public class ExpressionParser {

    private String expression;
    private int position;

    public double evaluate(String expression) throws CalculatorException {

        this.expression = expression
                .replaceAll("\\s+", "")
                .toLowerCase();

        this.position = 0;

        if (this.expression.isEmpty()) {
            throw new CalculatorException("Expression cannot be empty");
        }

        double result = parseExpression();

        if (position < expression.length()) {
            throw new CalculatorException(
                    "Invalid character: " + expression.charAt(position)
            );
        }

        return result;
    }

    // =========================================================
    // ADDITION AND SUBTRACTION
    // =========================================================

    private double parseExpression() throws CalculatorException {

        double result = parseTerm();

        while (position < expression.length()) {

            char operator = expression.charAt(position);

            if (operator == '+' || operator == '-') {

                position++;

                double next = parseTerm();

                if (operator == '+') {
                    result += next;
                } else {
                    result -= next;
                }

            } else {
                break;
            }
        }

        return result;
    }

    // =========================================================
    // MULTIPLICATION, DIVISION AND MODULUS
    // =========================================================

    private double parseTerm() throws CalculatorException {

        double result = parsePower();

        while (position < expression.length()) {

            char operator = expression.charAt(position);

            if (operator == '*'
                    || operator == '/'
                    || operator == '%') {

                position++;

                double next = parsePower();

                if (operator == '*') {

                    result *= next;

                } else if (operator == '/') {

                    if (next == 0) {
                        throw new CalculatorException(
                                "Division by zero"
                        );
                    }

                    result /= next;

                } else {

                    if (next == 0) {
                        throw new CalculatorException(
                                "Modulus by zero"
                        );
                    }

                    result %= next;
                }

            } else {
                break;
            }
        }

        return result;
    }

    // =========================================================
    // POWER AND FACTORIAL
    // =========================================================

    private double parsePower() throws CalculatorException {

        double result = parseFactor();

        while (position < expression.length()
                && expression.charAt(position) == '!') {

            position++;

            result = factorial(result);
        }

        if (position < expression.length()
                && expression.charAt(position) == '^') {

            position++;

            double exponent = parsePower();

            result = Math.pow(result, exponent);
        }

        return result;
    }

    // =========================================================
    // FACTORS
    // =========================================================

    private double parseFactor() throws CalculatorException {

        if (position >= expression.length()) {

            throw new CalculatorException(
                    "Expected a number"
            );
        }

        char current = expression.charAt(position);

        // Negative value
        if (current == '-') {

            position++;

            return -parseFactor();
        }

        // Parentheses
        if (current == '(') {

            position++;

            double result = parseExpression();

            if (position >= expression.length()
                    || expression.charAt(position) != ')') {

                throw new CalculatorException(
                        "Missing closing parenthesis"
                );
            }

            position++;

            return result;
        }

        // Function or constant
        if (Character.isLetter(current)) {

            return parseFunctionOrConstant();
        }

        return parseNumber();
    }

    // =========================================================
    // FUNCTIONS AND CONSTANTS
    // =========================================================

    private double parseFunctionOrConstant()
            throws CalculatorException {

        int start = position;

        while (position < expression.length()
                && Character.isLetter(expression.charAt(position))) {

            position++;
        }

        String name = expression.substring(start, position);

        // -----------------------------------------------------
        // CONSTANTS
        // -----------------------------------------------------

        if (name.equals("pi")) {
            return Math.PI;
        }

        if (name.equals("e")) {
            return Math.E;
        }

        // -----------------------------------------------------
        // FUNCTION MUST HAVE (
        // -----------------------------------------------------

        if (position >= expression.length()
                || expression.charAt(position) != '(') {

            throw new CalculatorException(
                    "Expected '(' after " + name
            );
        }

        position++;

        // -----------------------------------------------------
        // TWO-ARGUMENT FUNCTIONS
        // -----------------------------------------------------

        if (name.equals("pow")
                || name.equals("max")
                || name.equals("min")) {

            double first = parseExpression();

            if (position >= expression.length()
                    || expression.charAt(position) != ',') {

                throw new CalculatorException(
                        "Expected ',' between arguments"
                );
            }

            position++;

            double second = parseExpression();

            if (position >= expression.length()
                    || expression.charAt(position) != ')') {

                throw new CalculatorException(
                        "Missing ')' after " + name
                );
            }

            position++;

            switch (name) {

                case "pow":
                    return Math.pow(first, second);

                case "max":
                    return Math.max(first, second);

                case "min":
                    return Math.min(first, second);

                default:
                    throw new CalculatorException(
                            "Unknown function: " + name
                    );
            }
        }

        // -----------------------------------------------------
        // SINGLE-ARGUMENT FUNCTIONS
        // -----------------------------------------------------

        double value = parseExpression();

        if (position >= expression.length()
                || expression.charAt(position) != ')') {

            throw new CalculatorException(
                    "Missing ')' after " + name
            );
        }

        position++;

        switch (name) {

            case "sqrt":

                if (value < 0) {
                    throw new CalculatorException(
                            "Square root requires non-negative value"
                    );
                }

                return Math.sqrt(value);

            case "cbrt":

                return Math.cbrt(value);

            case "sin":

                return Math.sin(Math.toRadians(value));

            case "cos":

                return Math.cos(Math.toRadians(value));

            case "tan":

                return Math.tan(Math.toRadians(value));

            case "asin":

                if (value < -1 || value > 1) {
                    throw new CalculatorException(
                            "asin input must be between -1 and 1"
                    );
                }

                return Math.toDegrees(Math.asin(value));

            case "acos":

                if (value < -1 || value > 1) {
                    throw new CalculatorException(
                            "acos input must be between -1 and 1"
                    );
                }

                return Math.toDegrees(Math.acos(value));

            case "atan":

                return Math.toDegrees(Math.atan(value));

            case "sinh":

                return Math.sinh(value);

            case "cosh":

                return Math.cosh(value);

            case "tanh":

                return Math.tanh(value);

            case "log":

                if (value <= 0) {
                    throw new CalculatorException(
                            "Log requires positive value"
                    );
                }

                return Math.log10(value);

            case "ln":

                if (value <= 0) {
                    throw new CalculatorException(
                            "Ln requires positive value"
                    );
                }

                return Math.log(value);

            case "abs":

                return Math.abs(value);

            case "exp":

                return Math.exp(value);

            case "ceil":

                return Math.ceil(value);

            case "floor":

                return Math.floor(value);

            case "round":

                return Math.round(value);

            default:

                throw new CalculatorException(
                        "Unknown function: " + name
                );
        }
    }

    // =========================================================
    // NUMBER PARSER
    // =========================================================

    private double parseNumber()
            throws CalculatorException {

        int start = position;

        boolean decimalFound = false;

        while (position < expression.length()) {

            char current = expression.charAt(position);

            if (Character.isDigit(current)) {

                position++;

            } else if (current == '.' && !decimalFound) {

                decimalFound = true;

                position++;

            } else {

                break;
            }
        }

        if (start == position) {

            throw new CalculatorException(
                    "Expected number at position " + position
            );
        }

        try {

            return Double.parseDouble(
                    expression.substring(start, position)
            );

        } catch (NumberFormatException e) {

            throw new CalculatorException(
                    "Invalid number"
            );
        }
    }

    // =========================================================
    // FACTORIAL
    // =========================================================

    private double factorial(double value)
            throws CalculatorException {

        if (value < 0
                || value != Math.floor(value)) {

            throw new CalculatorException(
                    "Factorial requires a non-negative integer"
            );
        }

        if (value > 170) {

            throw new CalculatorException(
                    "Factorial value is too large"
            );
        }
        if(value >170){
            throw new CalculatorException(
                "Factorial value is too large"
            );
        }

        double result = 1;

        for (int i = 2; i <= (int) value; i++) {

            result *= i;
        }

        return result;
    }
}