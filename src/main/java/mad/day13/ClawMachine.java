package mad.day13;

public record ClawMachine(int a1, int a2, int b1, int b2, int c1, int c2) {

    // https://en.wikipedia.org/wiki/Cramer%27s_rule
    // Algèbre linéaire mon amour
    public int calcFewestTokens() {
        // Calculate determinant
        int det = a1 * b2 - b1 * a2;

        // If determinant is 0, system has no solution
        if (det == 0) {
            return 0;
        }

        // Calculate x and y using Cramer's rule
        float x = (float) (c1 * b2 - b1 * c2) / det;
        float y = (float) (a1 * c2 - c1 * a2) / det;

        float score = (3 * x) + y;

        // Check if x and y are natural numbers, sounds about right. right ?
        if (x % 1 == 0 && y % 1 == 0) {
            return (int) score;
        } else {
            return 0;
        }
    }

    public long calcFewestTokensPart2() {

        // Many zeros
        long bigC1 = c1 + 10_000_000_000_000L;
        long bigC2 = c2 + 10_000_000_000_000L;

        int det = a1 * b2 - b1 * a2;

        if (det == 0) {
            return 0;
        }

        double x = (double) (bigC1 * b2 - b1 * bigC2) / det;
        double y = (double) (a1 * bigC2 - bigC1 * a2) / det;

        if (x % 1 == 0 && y % 1 == 0) {
            return (long) (3*x + y);
        } else {
            return 0;
        }
    }
}
