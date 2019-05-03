package unipos.auth.components.right;

import java.util.List;
import java.util.Map;

/**
 * Created by Dominik on 28.05.2015.
 */
public interface RightService {

    void create(Right right);
    List<Right> findAll();

    List<RightDto> getRightsPerPartition();

    void addRightsToUser(List<String> rightGuids, Long userId);

    List<String> getRightGuidsOfUser(Long userId);

    void addRights(List<Right> rights);

    void deleteAllFromUser(Long userId);
}
