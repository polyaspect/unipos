package unipos.common.remote.data;

import unipos.common.remote.data.model.company.Store;

/**
 * Created by Joyce on 05.05.2016.
 */
public interface StoreRemoteInterface {

    Store findByUserId(String userId);
}
