package unipos.integritySafeGuard.event

import org.springframework.beans.factory.annotation.Autowired
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl

import javax.smartcardio.CardTerminal

/**
 * Created by dominik on 28.01.17.
 */
abstract class ScDetachedCommandImpl implements ScDetachedCommand {

    @Autowired
    SmartCardHandler smartCardHandler

    @Override
    void executeWithCard(List<CardTerminal> cardTerminals, List<String> removedCardReaders) {
        smartCardHandler.smartCards.removeIf({sc -> sc instanceof SmartCardImpl && removedCardReaders.contains(((SmartCardImpl)sc).readerName)})


        executeExternal(removedCardReaders)
    }

    abstract void executeExternal(List<String> detachedReaderName)
}
