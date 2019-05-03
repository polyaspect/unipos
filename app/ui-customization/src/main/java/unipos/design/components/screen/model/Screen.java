package unipos.design.components.screen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jolly on 04.11.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screen {
    private String name;
    private ScreenSettings settings;
    private Object data;
}
