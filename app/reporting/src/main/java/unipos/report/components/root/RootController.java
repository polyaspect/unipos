package unipos.report.components.root;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import unipos.common.container.PomVersionExplorer;

/**
 * Created by domin on 15.04.2016.
 */
@RestController
public class RootController {

    @RequestMapping(value = "/getCurrentVersion", method = RequestMethod.GET)
    public String getModuleVersion() {
        return PomVersionExplorer.getModuleVersion(this.getClass());
    }
}
