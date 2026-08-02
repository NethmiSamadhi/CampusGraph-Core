package testutil;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;
    private static final List<String> failures = new ArrayList<>();

    public static void assertEqual(Object expected, Object actual, String testName) {
        boolean ok = (expected == null && actual == null) || (expected != null && expected.equals(actual));
        record(ok, testName, "expected=" + expected + " actual=" + actual);
    }

    public static void assertTrue(boolean condition, String testName) {
        record(condition, testName, "expected true");
    }

    public static void assertFalse(boolean condition, String testName) {
        record(!condition, testName, "expected false");
    }

    public static void assertNull(Object obj, String testName) {
        record(obj == null, testName, "expected null, got " + obj);
    }

    public static void assertThrows(Runnable runnable, String testName) {
        boolean threw = false;
        try {
            runnable.run();
        } catch (Exception e) {
            threw = true;
        }
        record(threw, testName, "expected an exception to be thrown");
    }

    private static void record(boolean ok, String testName, String detail) {
        if (ok) {
            passed++;
            System.out.println("  [PASS] " + testName);
        } else {
            failed++;
            failures.add(testName + " -> " + detail);
            System.out.println("  [FAIL] " + testName + " -> " + detail);
        }
    }

    public static void printSummary() {
        System.out.println("\n==================================================");
        System.out.println("TEST SUMMARY: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
        if (!failures.isEmpty()) {
            System.out.println("Failures:");
            failures.forEach(f -> System.out.println("  - " + f));
        }
    }

    public static boolean hasFailures() {
        return failed > 0;
    }
}
