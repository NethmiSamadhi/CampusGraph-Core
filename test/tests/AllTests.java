package tests;

import testutil.TestRunner;

public class AllTests {
    public static void main(String[] args) {
        TestGraph.run();
        TestDijkstra.run();
        TestCongestion.run();
        TestMultiStopOptimizer.run();
        TestAccessibilityFilter.run();

        TestRunner.printSummary();

        if (TestRunner.hasFailures()) {
            System.exit(1);
        }
    }
}
