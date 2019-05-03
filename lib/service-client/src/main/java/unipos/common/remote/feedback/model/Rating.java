package unipos.common.remote.feedback.model;

import lombok.Data;

/**
 * Created by dominik on 14.11.15.
 */
@Data
public class Rating {
    private int id = -1;
    private String companyId;
    private String invoiceId;
    private int pointsAwarded = 0;
    private boolean rated = false;
}
