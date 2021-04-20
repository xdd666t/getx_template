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
    public boolean defaultMode = true;

    //default true
    public boolean useFolder = true;

    //default false
    public boolean usePrefix = false;

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

