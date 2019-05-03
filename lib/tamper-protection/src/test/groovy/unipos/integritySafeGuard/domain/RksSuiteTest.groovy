package unipos.integritySafeGuard.domain

import spock.lang.Specification

/**
 * Testing of the RKS-Suite
 * Created by domin on 30.09.2016.
 */
class RksSuiteTest extends Specification {

    def "Test getSuiteId"() {
        expect:
        ((RksSuite) x).suiteId == y

        where:
        x               || y
        RksSuite.R1_AT0 || "R1-AT0"
        RksSuite.R1_AT1 || "R1-AT1"
        RksSuite.R1_AT2 || "R1-AT2"
        RksSuite.R1_AT3 || "R1-AT3"
        RksSuite.R1_AT4 || "R1-AT4"
        RksSuite.R1_AT5 || "R1-AT5"
        RksSuite.R1_AT6 || "R1-AT6"
    }
}
