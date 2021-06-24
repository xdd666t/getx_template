package setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class SettingsComponent {
    public JPanel mainPanel;
    public JBTextField logicName;
    public JBTextField stateName;
    public JBTextField viewName;
    public JBTextField viewFileName;

    public SettingsComponent() {
        logicName = new JBTextField();
        stateName = new JBTextField();
        viewName = new JBTextField();
        viewFileName = new JBTextField();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Logic Name: "), logicName)
                .addLabeledComponent(new JBLabel("State Name: "), stateName)
                .addLabeledComponent(new JBLabel("View Name: "), viewName)
                .addLabeledComponent(new JBLabel("View File Name: "), viewFileName)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }
}
