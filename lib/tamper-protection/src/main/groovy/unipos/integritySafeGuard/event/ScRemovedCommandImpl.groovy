package unipos.integritySafeGuard.event

import org.springframework.beans.factory.annotation.Autowired
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl

import javax.smartcardio.CardTerminal
import java.util.stream.Collectors

/**
 * Created by domin on 22.01.2017.
 */
abstract class ScRemovedCommandImpl implements ScRemovedCommand {

    @Autowired
    SmartCardHandler smartCardHandler

    @Override
    void executeWithCard(List<CardTerminal> cardTerminals, List<String> readers) {
        Map<String, String> removedCardTerminals = new HashMap<>()
        cardTerminals.stream()
                .filter({x -> smartCardHandler.smartCards.stream().filter({y -> y instanceof SmartCardImpl}).map({y -> (SmartCardImpl)y}).anyMatch({y -> y.readerName == x.name})})
                .forEach({x -> removedCardTerminals.put(x.name, smartCardHandler.smartCards.stream().filter({y -> y instanceof SmartCardImpl}).map({y -> (SmartCardImpl)y}).filter({y -> y.readerName == x.name}).findFirst().get().certificateSerialNr.toUpperCase())})

        cardTerminals.each {smartCardHandler.smartCards.removeIf({sc -> sc instanceof SmartCardImpl && ((SmartCardImpl)sc).readerName == it.name})}
//        if(cardTerminals.size() > 0) {
//            println "REMOVED:"
//            SmartCardReaderMapping.instance.mapping.each {println it.key + " : " + it.value}
//        }

        executeExternal(removedCardTerminals)
    }

    abstract void executeExternal(Map<String, String> removedCardTerminals);
}
