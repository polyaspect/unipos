package unipos.design.components.screenCollection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import unipos.design.components.screen.model.Screen;

import java.util.List;

/**
 * Created by jolly on 04.11.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenCollection {
    @Id
    private String id;

    private String name;

    private List<Screen> screens;

    private ScreenCollectionSettings settings;

    private boolean published;

    private boolean standart;
}
