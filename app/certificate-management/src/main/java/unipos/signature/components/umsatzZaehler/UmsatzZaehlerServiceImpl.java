package unipos.signature.components.umsatzZaehler;

import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.signature.components.sequence.SequenceId;
import unipos.signature.components.sequence.SequenceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by domin on 15.12.2016.
 */

@Service
public class UmsatzZaehlerServiceImpl implements UmsatzZaehlerService {

    @Autowired
    UmsatzZaehlerRepository umsatzZaehlerRepository;
    @Autowired
    SequenceRepository sequenceRepository;

    @Override
    public UmsatzZaehler getLatestUmsatzZaehlerForStoreGuid(String storeGuid) {
        Assert.notNull(storeGuid, "The storeGuid must not be null");
        Assert.that(!storeGuid.isEmpty(), "No valid CompanyId given. Was empty!");

        UmsatzZaehler umsatzZaehler = umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(storeGuid);
        if(umsatzZaehler == null) {
            return UmsatzZaehler.builder()
                    .autoIncrement(0L)
                    .storeGuid(storeGuid)
                    .creationDateTime(null)
                    .umsatz(BigDecimal.ZERO)
                    .umsatzZaehler(BigDecimal.ZERO)
                    .guid(UUID.randomUUID().toString())
                    .build();
        } else {
            return umsatzZaehler;
        }
    }

    @Override
    public synchronized void saveUmsatzZaehler(UmsatzZaehler umsatzZaehler) {
        Assert.notNull(umsatzZaehler, "The UmsatzZaehler Entity must not be null");
        Assert.that(umsatzZaehler.getStoreGuid() != null && !umsatzZaehler.getStoreGuid().isEmpty(), "No valid storeGuid given. Was actually: " + umsatzZaehler.getStoreGuid());

        //Load the current umsatzZaehler amount from the db
        UmsatzZaehler currentUmsatzZaehler = umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(umsatzZaehler.getStoreGuid());
        //This holds the value that is currently saved in the db
        BigDecimal currentUmsatzZaehlerValue = null;
        if (currentUmsatzZaehler == null) {
            currentUmsatzZaehlerValue = BigDecimal.ZERO;
        } else {
            currentUmsatzZaehlerValue = currentUmsatzZaehler.getUmsatzZaehler();
        }

        Long autoIncrementingNumber = sequenceRepository.getNextSequenceId(SequenceId.Name.UMSATZ_ZAEHLER.name() + "_" + umsatzZaehler.getStoreGuid());

        umsatzZaehler.setAutoIncrement(autoIncrementingNumber);
        umsatzZaehler.setUmsatzZaehlerBefore(currentUmsatzZaehlerValue);
        umsatzZaehler.setUmsatzZaehler(currentUmsatzZaehlerValue.add(umsatzZaehler.getUmsatz()));
        umsatzZaehler.setCreationDateTime(LocalDateTime.now());
        umsatzZaehler.setGuid(UUID.randomUUID().toString());
        umsatzZaehlerRepository.saveSynchronised(umsatzZaehler);
    }

    @Override
    public void deleteByGuid(String guid) {
        umsatzZaehlerRepository.deleteByGuid(guid);
    }
}
