public class TrackTest {
    public static void main(String[] args) {
        Track monaco = Track.createMonacoTrack();
        Track monza = Track.createMonzaTrack();
        Track silverstone = Track.createSilverstoneTrack();

        System.out.println("Monaco: " + monaco);
        System.out.println("Track Rating: " + monaco.getTrackRating());

        System.out.println("\nMonza: " + monza);
        System.out.println("Track Rating: " + monza.getTrackRating());

        System.out.println("\nSilverstone: " + silverstone);
        System.out.println("Track Rating: " + silverstone.getTrackRating());
    }
}
