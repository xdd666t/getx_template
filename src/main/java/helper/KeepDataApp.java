package helper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.sun.org.apache.xpath.internal.operations.Bool;

//custom save location
//@com.intellij.openapi.components.State(name = "KeepDataApp", storages = {@Storage(value = "KeepDataApp.xml")})
public class KeepDataApp implements PersistentStateComponent<GetXState> {
    GetXState myState;

    @Override
    public GetXState getState() {
        return myState;
    }

    @Override
    public void loadState(GetXState state) {
        myState = state;
    }
}

