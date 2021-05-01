package setting;

import com.intellij.openapi.options.Configurable;
import helper.DataService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {

    private final DataService data = DataService.getInstance();
    private SettingsComponent mSetting;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GetX Setting";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mSetting = new SettingsComponent();
        return mSetting.mainPanel;
    }

    @Override
    public boolean isModified() {
        boolean modified;
        modified = !mSetting.logicName.getText().equals(data.logicName)
                || !mSetting.stateName.getText().equals(data.stateName)
                || !mSetting.viewName.getText().equals(data.viewName)
                || !mSetting.viewFileName.getText().equals(data.viewFileName);
        return modified;
    }

    @Override
    public void apply() {
        data.logicName = mSetting.logicName.getText();
        data.stateName = mSetting.stateName.getText();
        data.viewName = mSetting.viewName.getText();
        data.viewFileName = mSetting.viewFileName.getText();
    }

    @Override
    public void reset() {
        mSetting.logicName.setText(data.logicName);
        mSetting.stateName.setText(data.stateName);
        mSetting.viewName.setText(data.viewName);
        mSetting.viewFileName.setText(data.viewFileName);
    }

    @Override
    public void disposeUIResources() {
        mSetting = null;
    }
}
