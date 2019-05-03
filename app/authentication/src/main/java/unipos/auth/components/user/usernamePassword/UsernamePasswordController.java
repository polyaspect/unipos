package unipos.auth.components.user.usernamePassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.components.user.usernamePassword.UsernamePassword;

import java.util.List;

/**
 * Created by Dominik on 05.01.2016.
 */
@RestController
@RequestMapping("/usernamePassword")
public class UsernamePasswordController {

    @Autowired
    UsernamePasswordService usernamePasswordService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UsernamePassword> findAll() {
        return usernamePasswordService.findAll();
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public UsernamePassword findByUsername(@RequestParam String username) {
        return usernamePasswordService.findByUsername(username);
    }

    @RequestMapping(value = "findByUserGuid", method = RequestMethod.GET)
    public UsernamePassword findByUserGuid(@RequestParam String userGuid) {
        return usernamePasswordService.findByUserGuid(userGuid);
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveUsernamePassword(@RequestBody UsernamePassword usernamePassword) {
        if(usernamePassword.getId() == null || usernamePassword.getId().isEmpty()) {
            usernamePasswordService.saveCredentials(usernamePassword);
        } else {
            usernamePasswordService.updateCredentials(usernamePassword);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void removeUserFromUsernamePassword(@RequestBody UsernamePassword mitarbeiteridPin) {
        usernamePasswordService.removeUserFromMitarbeiterNrPin(mitarbeiteridPin);
    }
}
