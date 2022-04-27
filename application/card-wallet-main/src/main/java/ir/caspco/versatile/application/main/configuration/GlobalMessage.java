package ir.caspco.versatile.application.main.configuration;

import ir.caspco.versatile.correlation.RequestCorrelation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class GlobalMessage {

    @Builder.Default
    private boolean error = false;

    @Builder.Default
    private String statusKey = "SUCCESSFUL";

    @Builder.Default
    private UUID correlationId = RequestCorrelation.getId();

    private Object data;
}
