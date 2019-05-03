package unipos.design.components.screen.model;

import javafx.geometry.Orientation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jolly on 10.12.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSettings {
    private ScreenFunction function;
    private ScreenOrientation orientation;
    private ScreenSize size;
}
