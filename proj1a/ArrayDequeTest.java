public class ArrayDequeTest {
    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        // System.out.println("Make sure to uncomment the
        // lines below (and delete this print statement).");
        ///*
        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, lld1.isEmpty());

        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;

        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;

        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;

        System.out.println("Printing out deque: ");
        lld1.printDeque();

        printTestStatus(passed);
        //*/
    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        // System.out.println("Make sure to uncomment the
        // lines below (and delete this print statement).");
        ///*
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad.isEmpty());

        ad.addLast(0);
        // should not be empty
        passed = checkEmpty(false, ad.isEmpty()) && passed;

        ad.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad.isEmpty()) && passed;
        ad.addFirst(2);
        ad.removeLast();
        ad.addFirst(4);
        ad.addLast(6);
        ad.removeFirst();
        ad.removeFirst();
        ad.addFirst(9);
        ad.removeLast();
        ad.addFirst(11);
        ad.addLast(12);
        ad.addLast(13);
        ad.addLast(14);
        ad.addLast(15);
        ad.addLast(16);
        ad.addLast(18);
        ad.addLast(19);
        ad.addLast(21);
        ad.removeLast();
        ad.removeLast();

        printTestStatus(passed);
        //*/
    }

    public static void multiRemoveTest() {

        System.out.println("Running add/remove test.");

        // System.out.println("Make sure to uncomment the lines
        // below (and delete this print statement).");
        ///*
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad.isEmpty());

        ad.addLast(0);
        // should not be empty
        passed = checkEmpty(false, ad.isEmpty()) && passed;

        ad.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad.isEmpty()) && passed;
        ad.addFirst(2);
        ad.removeLast();
        ad.addFirst(4);
        ad.addLast(6);
        ad.removeFirst();
        ad.removeFirst();
        ad.addFirst(9);
        ad.removeLast();
        ad.addFirst(11);
        ad.addLast(12);
        ad.addLast(13);
        ad.addLast(14);
        ad.addLast(15);
        ad.addLast(16);
        ad.addLast(18);
        ad.addLast(19);
        ad.addLast(21);
        ad.removeLast();
        ad.removeLast();

        printTestStatus(passed);
        //*/
    }
    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        // addIsEmptySizeTest();
        addRemoveTest();
    }
}
