public class PerformanceCalculator {

    public static int calculateTopSpeed(Car car) {
        Engine engine = car.getEngine();
        AeroKit kit = car.getAeroKit();

        // Updated formula considering topSpeedImpact and engine power
        return (int) (engine.getPower() * 0.75 + kit.getTopSpeedImpact() - kit.getDragCoefficient() * 120);
    }

    public static double calculateAcceleration(Car car) {
        return car.getTotalWeight() / car.getEngine().getPower() * 6.0;
    }

    // ✅ Updated to accept weather and apply modifiers
    public static double calculateFuelConsumption(Car car, Track track) {
        Weather weather = track.getCurrentWeather();
        double base = track.getLength() / car.getEngine().getFuelEfficiency() + car.getAeroKit().getDragCoefficient() * 2;

        // 加雨天消耗 +15%
        if (weather != null && weather.getRainIntensity() > 0) {
            base *= 1.15;
        }

        // 风速 > 30 则增加 5%
        if (weather != null && weather.getWindSpeed() > 30) {
            base *= 1.05;
        }

        return base;
    }

    // ✅ Updated to account for weather grip modifier
    public static int calculateCorneringAbility(Car car, Track track) {
        double grip = (car.getFrontTyres().getGripLevel() + car.getRearTyres().getGripLevel()) * track.getEffectiveGrip();
        double aeroFactor = car.getAeroKit().getDownforce() / 50.0;
        return Math.min(10, (int) (grip * 5 + aeroFactor));
    }

    // ✅ Updated to use both car and track (with weather)
    public static Performance createCarPerformance(Car car, Track track) {
        int topSpeed = calculateTopSpeed(car);
        double acceleration = calculateAcceleration(car);
        double fuelConsumption = calculateFuelConsumption(car, track);
        double lapTime = track.getLength() * 25 + acceleration * 2; // Simple estimate
        int corneringAbility = calculateCorneringAbility(car, track);

        return new Performance(topSpeed, acceleration, fuelConsumption, lapTime, corneringAbility);
    }

    // ✅ AeroKit推荐逻辑：根据赛道属性选择最合适的空气动力学配置
    public static AeroKit getBestKitForTrack(Track track) {
        String difficulty = track.getDifficulty();
        int corners = track.getCorners();

        if (difficulty.equalsIgnoreCase("Hard") || corners > 15) {
            return AeroKitFactory.getKitByName("Extreme Aero Kit");
        } else if (difficulty.equalsIgnoreCase("Medium") && corners > 10) {
            return AeroKitFactory.getKitByName("Ground Effect Kit");
        } else {
            return AeroKitFactory.getKitByName("Low Drag Kit");
        }
    }
}
