package unipos.integritySafeGuard.domain
/**
 * This class represents the Rks definition that are required during the signature process
 * Created by Dominik on 31.09.2016
 */
enum RksSuite {

    R1_AT0("1", "AT0", "ES256", "SHA-256", 8, 3),
    R1_AT1("1", "AT1", "ES256", "SHA-256", 8, 3),
    R1_AT2("1", "AT2", "ES256", "SHA-256", 8, 3),
    R1_AT3("1", "AT3", "ES256", "SHA-256", 8, 3),
    R1_AT4("1", "AT4", "ES256", "SHA-256", 8, 3),
    R1_AT5("1", "AT5", "ES256", "SHA-256", 8, 3),
    R1_AT6("1", "AT6", "ES256", "SHA-256", 8, 3),
    R1_AT7("1", "AT7", "ES256", "SHA-256", 8, 3),
    R1_AT8("1", "AT8", "ES256", "SHA-256", 8, 3),
    R1_AT9("1", "AT9", "ES256", "SHA-256", 8, 3),
    R1_AT10("1", "AT10", "ES256", "SHA-256", 8, 3),

    // suite for an open system (in this case with the virtual ZDA identified by AT100)
    R1_AT100("1", "AT100", "ES256", "SHA-256", 8, 3);

    protected String suiteId;
    String zdaID;
    String jwsSignatureAlgorithm;
    String hashAlgorithmForPreviousSignatureValue;
    int numberOfBytesExtractedFromPrevSigHash;
    int aesKeyCheckExtractedBytes;

    RksSuite(String suiteID, String zdaID, String jwsSignatureAlgorithm, String hashAlgorithmForPreviousSignatureValue, int numberOfBytesExtractedFromPrevSigHash, int aesKeyCheckExtractedBytes) {
        this.suiteId = suiteID
        this.zdaID = zdaID
        this.jwsSignatureAlgorithm = jwsSignatureAlgorithm
        this.hashAlgorithmForPreviousSignatureValue = hashAlgorithmForPreviousSignatureValue
        this.numberOfBytesExtractedFromPrevSigHash = numberOfBytesExtractedFromPrevSigHash
        this.aesKeyCheckExtractedBytes = aesKeyCheckExtractedBytes
    }

    public String getSuiteId() {
        "R"+suiteId + "-" + zdaID
    }

//    @Override
//    String toString() {
//        getSuiteId()
//    }
}
