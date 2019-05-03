package unipos.authChecker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ggradnig on 16.10.2016.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NavigationSubMenuItem{
    private String text;
    private String hyperlink;
    private String right;
}