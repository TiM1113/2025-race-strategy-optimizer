public class TyreTest {

    public static void main(String[] args) {
        // Create and test Soft tyre
        Tyre soft = Tyre.createSoftTyre();
        System.out.println("Soft Tyre Info:");
        System.out.println(soft);
        System.out.println("Performance Rating: " + soft.getPerformanceRating());

        System.out.println("----------------------------------------");

        // Create and test Medium tyre
        Tyre medium = Tyre.createMediumTyre();
        System.out.println("Medium Tyre Info:");
        System.out.println(medium);
        System.out.println("Performance Rating: " + medium.getPerformanceRating());

        System.out.println("----------------------------------------");

        // Create and test Hard tyre
        Tyre hard = Tyre.createHardTyre();
        System.out.println("Hard Tyre Info:");
        System.out.println(hard);
        System.out.println("Performance Rating: " + hard.getPerformanceRating());
    }
}
