package unipos.signature.components.umsatzZaehler;

import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by domin on 22.01.2017.
 */

@RestController
@RequestMapping("/umsatzZaehler")
public class UmsatzZaehlerController {

    @Autowired
    UmsatzZaehlerService umsatzZaehlerService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public UmsatzZaehler getCurrentUmsatzZaehler(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No store found for the given Auth- and Device Token!");

        return umsatzZaehlerService.getLatestUmsatzZaehlerForStoreGuid(store.getGuid());
    }
}
