package unipos.common.remote.core;

import unipos.common.remote.auth.model.Right;
import unipos.common.remote.core.model.LicenseInfo;
import unipos.common.remote.core.model.Module;
import unipos.common.remote.core.model.ModuleStatus;
import unipos.common.remote.data.model.company.Store;

import java.util.List;

/**
 * Created by Joyce on 10.10.2015.
 */
public interface CoreRemoteInterface {
    List<Module> startedModules();

    boolean licenseFileExists();

    List<LicenseInfo> getAllLicenceInfos();

    void addNewAutoUpdaterSettlementDateTime(Store mapedStore);

    void deleteAutoUpdateSchedulerOfStore(Store map);

    String getCurrentVersion();

    ModuleStatus getModuleStatus(String moduleName);

    void addPermissions(List<Right> rights);
}
