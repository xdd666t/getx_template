package helper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

//custom save location
@com.intellij.openapi.components.State(
        name = "DataService",
        storages = {@Storage(value = "DataService.xml")
    })
public class DataService implements PersistentStateComponent<DataService> {
    //default true: use high mode
    public boolean defaultMode = GetXConfig.defaultMode;

    //default true
    public boolean useFolder = GetXConfig.useFolder;

    //default false
    public boolean usePrefix = GetXConfig.usePrefix;

    //Logical layer name
    public String logicName = GetXConfig.logicName;

    public static DataService getInstance() {
        return ServiceManager.getService(DataService.class);
    }

    @Override
    public DataService getState() {
        return this;
    }

    @Override
    public void loadState(DataService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}

