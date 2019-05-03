package unipos.core.components.navigation;

import unipos.authChecker.domain.NavigationMenuItem;

import java.util.List;

/**
 * Created by ggradnig on 16.10.2016.
 */
public interface NavigationService {
    List<NavigationMenuItem> runningModules(String authToken) throws Exception;
}
