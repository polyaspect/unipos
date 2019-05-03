package unipos.integritySafeGuard.smartcard

import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.springframework.util.Assert
import unipos.integritySafeGuard.SmardCardException
import unipos.integritySafeGuard.SmartCardUtils

import javax.smartcardio.ATR
import javax.smartcardio.Card
import javax.smartcardio.CardException
import javax.smartcardio.CardTerminal
import javax.smartcardio.CardTerminals
import javax.smartcardio.TerminalFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.security.NoSuchAlgorithmException
import java.util.stream.Collectors

/**
 * Created by domin on 21.09.2016.
 */
class SmartCardHandler {

    List<SmartCard> smartCards = new ArrayList<>()

    static List<CardTerminal> getAttachedCards() {
        TerminalFactory terminalFactory;
        try {
            terminalFactory = TerminalFactory.getInstance("PC/SC", null);
        } catch (NoSuchAlgorithmException e) {
            terminalFactory = TerminalFactory.getDefault();
        }
        CardTerminals cardTerminals = terminalFactory.terminals();
        try {
            return cardTerminals.list(CardTerminals.State.CARD_INSERTION)
        } catch (CardException e) {
            return new ArrayList<CardTerminal>()
        }
    }

    List<SmartCard> getAttachedSmartCards() {
        def cards

        try {
            cards = getAttachedCards()
        } catch (Exception ex) {
//            cleanTerminalFactory()
            cards = getAttachedCards()
        }
        if (cards && cards.size() > 0) {
            cards.stream().map({c -> new SmartCardImpl(c)}).collect(Collectors.toList())
        } else {
            new ArrayList<SmartCard>()
        }
    }

    SmartCard getSmartCardBySerialNo(String serialNo) {
        //first check, of we already have an existing instance for this SerialNo. First check if theres an smart card with an open connection.
        //Then check if theres a mock connection. This connection retry to establish. Because maybe we already got an open connection :-)
        if (smartCards.stream().anyMatch({ smartCard -> smartCard.certificateSerialNr == serialNo.toUpperCase() && smartCard instanceof SmartCardImpl})) {
            return smartCards.stream().filter({smartCard -> smartCard.certificateSerialNr == serialNo.toUpperCase() && smartCard instanceof SmartCardImpl}).findFirst().get()
        } else if (smartCards.stream().anyMatch({smartCard -> smartCard.certificateSerialNr == serialNo.toUpperCase()})) {
            return smartCards.stream().filter({smartCard -> smartCard.certificateSerialNr == serialNo.toUpperCase()}).findFirst().get()
        }

        def smartCards = getAttachedSmartCards()
        def smartCard = smartCards.stream().filter({x -> (x.certificateSerialHex.toUpperCase() == serialNo.toUpperCase()) })
        .findFirst()
        .orElse(new SmartCardMock(serialNo.toUpperCase()))

        return smartCard
    }

    static cleanTerminalFactory() {
        Class pcscterminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
        Field contextId = pcscterminal.getDeclaredField("contextId");
        contextId.setAccessible(true);

        if(contextId.getLong(pcscterminal) != 0L)
        {
            // First get a new context value
            Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
            Method SCardEstablishContext = pcsc.getDeclaredMethod(
                    "SCardEstablishContext",
                    [Integer.TYPE ] as Class[]
            );
            SCardEstablishContext.setAccessible(true);

            Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
            SCARD_SCOPE_USER.setAccessible(true);

            long newId = ((Long)SCardEstablishContext.invoke(pcsc,
                    [SCARD_SCOPE_USER.getInt(pcsc) ] as Object[]
            ));
            contextId.setLong(pcscterminal, newId);


            // Then clear the terminals in cache
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            Field fieldTerminals = pcscterminal.getDeclaredField("terminals");
            fieldTerminals.setAccessible(true);
            Class classMap = Class.forName("java.util.Map");
            Method clearMap = classMap.getDeclaredMethod("clear");

            clearMap.invoke(fieldTerminals.get(terminals));
        }
    }
}
