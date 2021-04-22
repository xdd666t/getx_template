package setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class SettingsComponent {

    private JPanel mainPanel;
    private JBTextField logicName;

    public SettingsComponent() {
        logicName = new JBTextField();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Logic Name: "), logicName)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public String getLogicName() {
        return logicName.getText();
    }

    public void setLogicName(String text) {
        logicName.setText(text);
    }
}
