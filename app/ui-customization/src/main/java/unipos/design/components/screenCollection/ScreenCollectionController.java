package unipos.design.components.screenCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.design.components.screenCollection.model.ScreenCollection;

import java.util.List;

/**
 * Created by jolly on 10.12.2016.
 */
@RestController
@RequestMapping(value = "/screenCollection", produces = "application/json;charset=UTF-8")
public class ScreenCollectionController {
     @Autowired
     ScreenCollectionService screenCollectionService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ScreenCollection> findAll() {
        return screenCollectionService.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ScreenCollection save(@RequestBody ScreenCollection screenCollection) {
        return screenCollectionService.save(screenCollection);
    }

    @RequestMapping(value = "/{screenCollectionName}/{published}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ScreenCollection findByNameAndPublished(@PathVariable String screenCollectionName, @PathVariable boolean published){
        return screenCollectionService.findByNameAndPublished(screenCollectionName, published);
    }

    @RequestMapping(value = "/listNames/{published}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<String> listNames(@PathVariable boolean published){
        return screenCollectionService.listnamesByPublished(published);
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ScreenCollection publish(@RequestBody String screenCollectionName){
        return screenCollectionService.publish(screenCollectionName);
    }
    @RequestMapping(value = "/default", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ScreenCollection setDefault(@RequestBody String screenCollectionName){
        return screenCollectionService.setDefault(screenCollectionName);
    }
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ScreenCollection getDefault(){
        return screenCollectionService.findFirstByPublished();
    }
}
