package unipos.integritySafeGuard.formatter

/**
 * Created by Dominik on 02.09.2016.
 */
abstract class Formatter {

    //One byte equals two characters. There are 8 bytes required = 8 --> 8*2=16 chars!!!
    private final int pinLengthInByte = 16

    String formatPin(String pin) {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append("2")
        stringBuilder.append(pin.length())
        stringBuilder.append(pin)

        stringBuilder.toString().padRight(pinLengthInByte, 'f')
    }
}