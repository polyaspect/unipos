package unipos.authChecker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by ggradnig on 16.10.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NavigationMenuItem {
    private String right;
    private String text;
    private String hyperlink;
    private String icon;
    private int order;
    private NavigationSubMenuItem[] subItems;
}
