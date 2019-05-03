package unipos.auth.components.right;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by domin on 21.05.2016.
 */
@RestController
@RequestMapping("/rights")
public class RightController {

    @Autowired
    RightService rightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Right> findAll() {
        return rightService.findAll();
    }

    @RequestMapping(value = "/rightsPerPartition", method = RequestMethod.GET)
    public List<RightDto> getRightsPerPartition() {
        return rightService.getRightsPerPartition();
    }

    @RequestMapping(value = "/rightsOfUser/{userId}", method = RequestMethod.GET)
    public List<String> getRightGuidsOfUser(@PathVariable("userId") Long userId) {
        return rightService.getRightGuidsOfUser(userId);
    }

    @RequestMapping(value = "/assignRightsToUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void assignRightsToUser(@RequestParam Long userId, @RequestParam(required = false) List<String> rightGuids) {
        if(rightGuids == null ||rightGuids.isEmpty()) {
            rightService.deleteAllFromUser(userId);
        } else {
            rightService.addRightsToUser(rightGuids, userId);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addRights(@RequestBody List<Right> rights) {
        rightService.addRights(rights);
    }
}
