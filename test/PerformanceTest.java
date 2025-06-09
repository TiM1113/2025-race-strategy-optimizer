// PerformanceTest.java
public class PerformanceTest {
    public static void main(String[] args) {

        // Create two performance samples
        Performance perf1 = new Performance(280, 3.2, 2.5, 92.3, 8);
        Performance perf2 = new Performance(260, 3.5, 2.8, 95.0, 7);

        // Print both performances
        System.out.println("Performance 1:");
        System.out.println(perf1);
        System.out.println("Overall Rating: " + perf1.getOverallRating());

        System.out.println("\nPerformance 2:");
        System.out.println(perf2);
        System.out.println("Overall Rating: " + perf2.getOverallRating());

        // Compare lap times
        System.out.println("\nPerformance 1 has shorter lap time than Performance 2: " + perf1.isFewerThan(perf2));
    }
}
