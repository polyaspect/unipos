package unipos.report.components.review;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dominik on 10.10.15.
 */

@RequestMapping("/reviews")
@RestController
public class ReviewController {

    @RequestMapping(value = "/journalReport", method = RequestMethod.GET)
    public void generateJournalReport() {

    }

}
