package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface SegmentMapService {

    Map<String, String> segmentMap = new HashMap<>();

    String toSegment(String name);

    boolean isAcceptableSegment(String name);
}
