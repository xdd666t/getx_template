package setting;

import com.intellij.openapi.options.Configurable;
import helper.DataService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {

    private DataService data = DataService.getInstance();
    private SettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GetX Setting";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new SettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        boolean modified;
        modified = !mySettingsComponent.getLogicName().equals(data.logicName);
        return modified;
    }

    @Override
    public void apply() {
        data.logicName = mySettingsComponent.getLogicName();
    }

    @Override
    public void reset() {
        mySettingsComponent.setLogicName(data.logicName);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
