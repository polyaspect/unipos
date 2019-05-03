package unipos.integritySafeGuard.config

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import sun.security.smartcardio.PCSCException
import unipos.integritySafeGuard.event.ScDetachedCommand
import unipos.integritySafeGuard.event.ScInsertedCommand
import unipos.integritySafeGuard.event.ScInsertedCommandImpl
import unipos.integritySafeGuard.event.ScRemovedCommand
import unipos.integritySafeGuard.event.ScRemovedCommandImpl
import unipos.integritySafeGuard.event.SmartCardReaderMapping
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.smartcardio.CardException
import javax.smartcardio.CardTerminals
import javax.smartcardio.TerminalFactory
import java.security.Security
import java.util.stream.Collectors

/**
 * Created by domin on 16.09.2016.
 */
class Setup {

    @Autowired
    ScInsertedCommand scInsertedCommand
    @Autowired
    ScRemovedCommand scRemovedCommand
    @Autowired
    ScDetachedCommand scDetachedCommand
    @Autowired
    SmartCardHandler smartCardHandler

    static boolean exit = false

    @Bean
    ScEventListener scEventListener() {
        new ScEventListener()
    }

    @PostConstruct
    void setup() {
        Security.removeProvider("BC")
        Security.addProvider(new BouncyCastleProvider());

        if (!scInsertedCommand ||!scRemovedCommand || scDetachedCommand) {
            Thread.sleep(3000l)
        }

        //startup lookup for smartCards begin
        try {

            TerminalFactory terminalFactory

            try {
                terminalFactory = TerminalFactory.getInstance("PC/SC", null)
            } catch (Exception ex) {
                terminalFactory = TerminalFactory.getDefault()
            }

            def insertedCards = terminalFactory.terminals().list(CardTerminals.State.CARD_INSERTION)
            def removedCards = terminalFactory.terminals().list(CardTerminals.State.CARD_REMOVAL)

            def readersAvailable = terminalFactory.terminals().list().stream().map({reader -> reader.name}).collect(Collectors.toList())
            def removedReaders = smartCardHandler.smartCards.stream().filter({y -> y instanceof SmartCardImpl}).map({ y -> (SmartCardImpl)y}).map({ sc -> sc.readerName}).filter({ x -> !readersAvailable.contains(x)}).collect(Collectors.toList())


            if (insertedCards.size() > 0) {
                scInsertedCommand.executeWithCard(insertedCards, removedReaders)
            }
            if (removedCards.size() > 0) {
                scRemovedCommand.executeWithCard(removedCards, removedReaders)
            }
        } catch (Exception ignored) {
            println "Reset Terminal Factory"
            ignored.printStackTrace()
//            SmartCardHandler.cleanTerminalFactory()
            Thread.sleep(10000l)
        }
        //startup lookup end

        if (scInsertedCommand && scRemovedCommand) {
            new Thread(scEventListener()).start()
        }
    }

    @PreDestroy
    void preDestroy() {
        exit = true
        Thread.sleep(3000l)
    }

    class ScEventListener implements Runnable {

        TerminalFactory  terminalFactory = null

        ScEventListener() {
            try {
                terminalFactory = TerminalFactory.getInstance("PC/SC", null)
            } catch (Exception ex) {
                terminalFactory = TerminalFactory.getDefault()
            }
        }

        void run() {
            while(!exit) {
                try {
                    try {
                        if (!terminalFactory.terminals().waitForChange(100l)) {
                            Thread.sleep(100l);
                            continue
                        }
                    } catch (Exception ignored) {
                        Thread.sleep(100l);
                        SmartCardHandler.cleanTerminalFactory();
                        continue
                    }

                    def insertedCards = terminalFactory.terminals().list(CardTerminals.State.CARD_INSERTION)
                    def removedCards = terminalFactory.terminals().list(CardTerminals.State.CARD_REMOVAL)

                    def readersAvailable = terminalFactory.terminals().list().stream().map({reader -> reader.name}).collect(Collectors.toList())
                    def removedReaders = smartCardHandler.smartCards.stream().filter({y -> y instanceof SmartCardImpl}).map({ y -> (SmartCardImpl)y}).map({ sc -> sc.readerName}).filter({ x -> !readersAvailable.contains(x)}).collect(Collectors.toList())

                    if(removedReaders.size() > 0) {
                        scDetachedCommand.executeWithCard(readersAvailable, removedReaders)
                    }
                    if (insertedCards.size() > 0) {
                        scInsertedCommand.executeWithCard(insertedCards, removedReaders)
                    }
                    if (removedCards.size() > 0) {
                        scRemovedCommand.executeWithCard(removedCards, removedReaders)
                    }
                } catch (Exception ignored) {
                    println "Reset Terminal Factory"
                    ignored.printStackTrace()
//                    SmartCardHandler.cleanTerminalFactory()
                    Thread.sleep(10000l)
                }
            }
            println "CardDetector Thread exiting!!!"
        }
    }
}
