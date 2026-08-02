package ui.gui;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Fixed x/y coordinates for drawing each campus location on the blueprint
 * canvas. Positions are laid out by hand to roughly mirror the sample
 * campus's real connectivity (see CampusMapData), so the drawn map reads
 * as a plausible walkable layout rather than an arbitrary node scatter.
 */
public final class GraphLayout {
    private static final Map<String, Point> POSITIONS = new HashMap<>();

    static {
        POSITIONS.put("GATE", new Point(90, 300));
        POSITIONS.put("PARK", new Point(90, 460));
        POSITIONS.put("ADMIN", new Point(270, 170));
        POSITIONS.put("LIB", new Point(460, 170));
        POSITIONS.put("CAF", new Point(460, 390));
        POSITIONS.put("LAB1", new Point(650, 170));
        POSITIONS.put("LH1", new Point(650, 320));
        POSITIONS.put("LH2", new Point(650, 470));
        POSITIONS.put("SPORT", new Point(830, 400));
    }

    private GraphLayout() {}

    public static Point get(String nodeId) {
        return POSITIONS.getOrDefault(nodeId, new Point(50, 50));
    }
}
