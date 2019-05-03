package unipos.core.components.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import unipos.authChecker.domain.NavigationMenuItem;
import unipos.common.container.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Dominik on 25.06.2015.
 */
@RestController
@RequestMapping("/nav")
public class NavigationController {
    @Autowired
    private NavigationService navigationService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NavigationMenuItem> nav(HttpServletRequest request) throws Exception{
        return navigationService.runningModules(RequestHandler.getAuthToken(request));
    }

}
