package unipos.integritySafeGuard.domain.exceptions

/**
 * Created by domin on 14.01.2017.
 */
class SignatureRequiredException extends Exception {

    SignatureRequiredException() {
        super()
    }

    SignatureRequiredException(String s) {
        super(s)
    }
}
