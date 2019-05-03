package unipos.auth.components.right;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by domin on 21.05.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RightTypeDto {
    private String name;
    private String guid;
    private boolean selected;
}
