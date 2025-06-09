import java.util.ArrayList;
import java.util.List;

public class AeroKitFactory {

    public static List<AeroKit> getAllAvailableKits() {
        List<AeroKit> kits = new ArrayList<>();
        kits.add(AeroKit.createStandardKit());
        kits.add(AeroKit.createHighDownforceKit());
        kits.add(AeroKit.createLowDragKit());
        kits.add(AeroKit.createAdjustableKit());
        kits.add(AeroKit.createGroundEffectKit());
        kits.add(AeroKit.createExtremeAeroKit());
        return kits;
    }

    public static AeroKit getKitByName(String name) {
        for (AeroKit kit : getAllAvailableKits()) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public static List<AeroKit> getKitsForTrackType(String trackType) {
        List<AeroKit> recommended = new ArrayList<>();
        switch (trackType.toLowerCase()) {
            case "highspeed":
                recommended.add(AeroKit.createLowDragKit());
                recommended.add(AeroKit.createStandardKit());
                break;
            case "technical":
                recommended.add(AeroKit.createHighDownforceKit());
                recommended.add(AeroKit.createGroundEffectKit());
                break;
            case "balanced":
            default:
                recommended.add(AeroKit.createAdjustableKit());
                recommended.add(AeroKit.createStandardKit());
                break;
        }
        return recommended;
    }
}
