package unipos.integritySafeGuard.event

import org.springframework.beans.factory.annotation.Autowired
import unipos.integritySafeGuard.smartcard.SmartCard
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl

import javax.smartcardio.Card
import javax.smartcardio.CardException
import javax.smartcardio.CardTerminal
import java.util.stream.Collectors

/**
 * Created by domin on 22.01.2017.
 */
abstract class ScInsertedCommandImpl implements ScInsertedCommand {

    @Autowired
    SmartCardHandler smartCardHandler

    @Override
    void executeWithCard(List<CardTerminal> cardTerminals, List<String> readers) {
        Map<String, SmartCardImpl> cards = new HashMap<>()
        Map<String, String> updatedCards = new HashMap<>()

        for (CardTerminal cardTerminal in cardTerminals) {
            try {
                SmartCardImpl smartCardImpl = new SmartCardImpl(cardTerminal)
                cards.put(cardTerminal.name, smartCardImpl)
            } catch (CardException e) {
                e.printStackTrace()
            }
        }

        Thread.sleep(500L)


        cards.keySet().stream().forEach({key ->
            def card = cards.get(key)
            String crtSerial = card.certificateSerialNr.toUpperCase()
            if(!smartCardHandler.smartCards.stream().anyMatch({smartCard -> smartCard.certificateSerialNr == crtSerial})) {
                smartCardHandler.smartCards.add(card)
                updatedCards.put(key, crtSerial)
            }
//            if (!SmartCardReaderMapping.instance.mapping.containsKey(key)) {
//                SmartCardReaderMapping.instance.mapping.put(key, crtSerial)
//                updatedCards.put(key, crtSerial)
//            }
        })

//        if (cardTerminals.size() > 0) {
//            println "ADDED:"
//            SmartCardReaderMapping.instance.mapping.each {value -> println value.key + " : " + value.value}
//        }

        executeExternal(updatedCards)
    }

    abstract void executeExternal(Map<String, String> updatedCards);
}
