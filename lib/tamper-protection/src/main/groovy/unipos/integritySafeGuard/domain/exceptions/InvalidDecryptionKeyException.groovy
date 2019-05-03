package unipos.integritySafeGuard.domain.exceptions

/**
 * Created by domin on 16.09.2016.
 */
class InvalidDecryptionKeyException extends Exception {

    public InvalidDecryptionKeyException() {
        super()
    }

    public InvalidDecryptionKeyException(String message) {
        super(message ?: "Unable to encrypt the correct value for the standUmsatzZaehlerField. Have you entered the correct Decryption-Key?")
    }
}
