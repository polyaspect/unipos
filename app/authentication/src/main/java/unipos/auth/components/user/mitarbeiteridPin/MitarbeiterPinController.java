package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dominik on 05.01.2016.
 */
@RestController
@RequestMapping("/mitarbeiterPin")
public class MitarbeiterPinController {

    @Autowired
    MitarbeiteridPinService mitarbeiteridPinService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MitarbeiteridPin> findAll() {
        return mitarbeiteridPinService.findAll();
    }

    @RequestMapping(value = "/mitarbeiterId", method = RequestMethod.GET)
    public MitarbeiteridPin findByMitarbeiterid(@RequestParam int mitarbeiterId) {
        return mitarbeiteridPinService.findByMitarbeiterNr(mitarbeiterId);
    }

    @RequestMapping(value = "findByUserGuid", method = RequestMethod.GET)
    public MitarbeiteridPin findByUserGuid(@RequestParam String userGuid){
        return mitarbeiteridPinService.findByUserGuid(userGuid);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveMitarbeiterPin(@RequestBody MitarbeiteridPin mitarbeiteridPin, HttpServletResponse response) throws IOException {
        if(mitarbeiteridPin.getId() == null || mitarbeiteridPin.getId().isEmpty()) {
            try {
                mitarbeiteridPinService.saveCredentials(mitarbeiteridPin);
            } catch (IllegalArgumentException e) {
                response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
                return;
            }
        } else {
            try {
                mitarbeiteridPinService.updateCredentials(mitarbeiteridPin);
            } catch (IllegalArgumentException e) {
                response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
                return;
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void removeUserFromMitarbeiterPin(@RequestBody MitarbeiteridPin mitarbeiteridPin) {
        mitarbeiteridPinService.removeUserFromMitarbeiterNrPin(mitarbeiteridPin);
    }
}
