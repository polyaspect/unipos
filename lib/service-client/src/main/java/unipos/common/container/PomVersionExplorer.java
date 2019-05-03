package unipos.common.container;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by domin on 15.04.2016.
 */
public class PomVersionExplorer {

    public static String getModuleVersion(Class clazz) {
        return clazz.getPackage().getImplementationVersion();
    }
}
