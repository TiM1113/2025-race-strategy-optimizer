public class RaceStrategyTest {
    public static void main(String[] args) {
        RaceStrategy aggressive = RaceStrategy.createAggressiveStrategy();
        RaceStrategy balanced = RaceStrategy.createBalancedStrategy();
        RaceStrategy conservative = RaceStrategy.createConservativeStrategy();

        System.out.println("Aggressive Strategy:");
        System.out.println(aggressive);
        System.out.println("Is Conservative? " + aggressive.isConservativeStrategy());

        System.out.println("\nBalanced Strategy:");
        System.out.println(balanced);
        System.out.println("Is Conservative? " + balanced.isConservativeStrategy());

        System.out.println("\nConservative Strategy:");
        System.out.println(conservative);
        System.out.println("Is Conservative? " + conservative.isConservativeStrategy());
    }
}
