public class Track {
    private String name;
    private double length; // in kilometers
    private int corners;
    private String difficulty; // "Easy", "Medium", "Hard"
    private String surfaceType; // "Smooth", "Rough"
    private Weather currentWeather; // ✅ 新增属性

    // Constructor
    public Track(String name, double length, int corners, String difficulty, String surfaceType) {
        this.name = name;
        this.length = length;
        this.corners = corners;
        this.difficulty = difficulty;
        this.surfaceType = surfaceType;
        this.currentWeather = Weather.createDryWeather(); // 默认天气为干燥
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public int getCorners() {
        return corners;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    // 返回弯道评分（供策略使用）
    public double getTrackRating() {
        return corners * length;
    }

    // ✅ 新增方法：根据天气条件返回有效抓地力影响（0.0~1.0）
    public double getEffectiveGrip() {
        if (currentWeather != null && currentWeather.getRainIntensity() > 5) {
            return 0.8; // 雨强 > 5 时，抓地力下降 20%
        }
        return 1.0; // 正常抓地力
    }

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", length=" + length +
                " km, corners=" + corners +
                ", difficulty='" + difficulty + '\'' +
                ", surfaceType='" + surfaceType + '\'' +
                ", weather=" + currentWeather +
                '}';
    }

    // 静态工厂方法
    public static Track createMonacoTrack() {
        return new Track("Monaco", 3.3, 19, "Hard", "Smooth");
    }

    public static Track createMonzaTrack() {
        return new Track("Monza", 5.8, 11, "Medium", "Smooth");
    }

    public static Track createSilverstoneTrack() {
        return new Track("Silverstone", 5.9, 18, "Medium", "Smooth");
    }
}
