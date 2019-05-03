package unipos.design.components.screenCollection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by jolly on 22.01.2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenCollectionSettings {
    private List<String> stores;
}
