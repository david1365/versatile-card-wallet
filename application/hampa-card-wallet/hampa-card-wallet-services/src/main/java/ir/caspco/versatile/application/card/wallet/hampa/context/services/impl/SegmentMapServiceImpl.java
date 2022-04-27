package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.SegmentMapService;
import org.springframework.stereotype.Service;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
public class SegmentMapServiceImpl implements SegmentMapService {

    static {

        segmentMap.put("OTR", "10");
        segmentMap.put("10", "OTR");
    }

    @Override
    public String toSegment(String name) {

        String segmentCode = segmentMap.get(name);
        if (segmentCode == null) {

            throw new WalletNotFoundException();
        }

        return segmentCode;
    }

    @Override
    public boolean isAcceptableSegment(String name) {

        return segmentMap.get(name) != null;
    }
}
