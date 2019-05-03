package unipos.core.components.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.authChecker.domain.NavigationMenuItem;
import unipos.common.container.UrlContainer;
import unipos.core.components.modules.Module;
import unipos.core.components.modules.ModuleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggradnig on 16.10.2016.
 */

@Service
public class NavigationServiceImpl implements  NavigationService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModuleService moduleService;

    @Override
    public List<NavigationMenuItem> runningModules(String authToken) throws Exception{
        List<NavigationMenuItem> menuItems = new ArrayList<>();
        for(Module module : moduleService.getRunningModules()){
            if(module.getName().equals("manager"))
                continue;
            try{
                String url = "";
                if(!module.getName().equals("core"))
                    url = UrlContainer.BASEURL + "/" + module.getName() + "/navItem/" + authToken;
                else
                    url = UrlContainer.BASEURL + "/navItem/" + authToken;
                NavigationMenuItem item = restTemplate.getForObject(url, NavigationMenuItem.class);
                if(item != null) {
                    menuItems.add(item);
                }
            }
            catch(Exception ex){

            }
        }
        return menuItems;
    }
}
