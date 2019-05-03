package unipos.auth.components.right;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by domin on 21.05.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RightDto {

    private String partition;
    private List<RightTypeDto> types;
}
