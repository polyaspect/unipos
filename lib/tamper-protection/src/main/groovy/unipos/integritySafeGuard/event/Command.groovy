package unipos.integritySafeGuard.event

import javax.smartcardio.CardTerminal

/**
 * Created by domin on 22.01.2017.
 */
interface Command {

    void executeWithCard(List<CardTerminal> cardTerminals, List<String> readerNames)
}
