package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.SegmentMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {
        SegmentMapServiceImpl.class
})
class SegmentMapServiceImplTest {

    @Autowired
    private SegmentMapService segmentMapService;

    @Test
    void toSegment() {

        assertEquals("10", segmentMapService.toSegment("OTR"));
        assertEquals("OTR", segmentMapService.toSegment("10"));

        try {

            segmentMapService.toSegment("01");
            fail();
        } catch (Exception e) {

            assertTrue(e instanceof WalletNotFoundException);
        }
    }

    @Test
    void isAcceptableSegment() {

        assertTrue(segmentMapService.isAcceptableSegment("10"));
        assertTrue(segmentMapService.isAcceptableSegment("OTR"));

        assertFalse(segmentMapService.isAcceptableSegment("TSM"));
    }
}