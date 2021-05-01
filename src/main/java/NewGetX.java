import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import helper.DataService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;


public class NewGetX extends AnAction {
    private Project project;
    private String psiPath;
    private DataService data;

    /**
     * Overall popup entity
     */
    private JDialog jDialog;
    private JTextField nameTextField;
    private ButtonGroup templateGroup;
    /**
     * Checkbox
     * Use folder：default true
     * Use prefix：default false
     */
    private JCheckBox folderBox, prefixBox, disposeBox;

    @Override
    public void actionPerformed(AnActionEvent event) {
        project = event.getProject();
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString();
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1);
        initData();
        initView();
    }

    private void initData() {
        data = DataService.getInstance();
        jDialog = new JDialog(new JFrame(), "GetX Template Code Produce");
    }

    private void initView() {
        //Set function button
        Container container = jDialog.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        //Set the main module style: mode, function
        //deal default value
        setModule(container);

        //Setting options: whether to use prefix
        //deal default value
        setCodeFile(container);

        //Generate file name and OK cancel button
        setNameAndConfirm(container);

        //Choose a pop-up style
        setJDialog();
    }

    /**
     * generate  file
     */
    private void save() {
        if (nameTextField.getText() == null || "".equals(nameTextField.getText().trim())) {
            Messages.showInfoMessage(project, "Please input the module name", "Info");
            return;
        }
        dispose();
        //Create a file
        createFile();
        //Refresh project
        project.getBaseDir().refresh(false, true);
    }

    /**
     * Set the overall pop-up style
     */
    private void setJDialog() {
        //The focus is on the current pop-up window,
        // and the focus will not shift even if you click on other areas
        jDialog.setModal(true);
        //Set padding
        ((JPanel) jDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jDialog.setSize(400, 335);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    /**
     * Main module
     */
    private void setModule(Container container) {
        //Two rows and two columns
        JPanel template = new JPanel();
        template.setLayout(new GridLayout(1, 2));
        //Set the main module style：mode, function
        template.setBorder(BorderFactory.createTitledBorder("Select Mode"));
        //default: high setting
        JRadioButton defaultBtn = new JRadioButton("Default", data.defaultMode);
        defaultBtn.setActionCommand("Default");
        setPadding(defaultBtn, 5, 10);
        JRadioButton highBtn = new JRadioButton("Easy", !data.defaultMode);
        setPadding(highBtn, 5, 10);
        highBtn.setActionCommand("Easy");


        template.add(defaultBtn);
        template.add(highBtn);
        templateGroup = new ButtonGroup();
        templateGroup.add(defaultBtn);
        templateGroup.add(highBtn);

        container.add(template);
        setDivision(container);
    }

    /**
     * Generate file
     */
    private void setCodeFile(Container container) {
        //Select build file
        JPanel file = new JPanel();
        file.setLayout(new GridLayout(2, 2));
        file.setBorder(BorderFactory.createTitledBorder("Select Function"));

        //use folder
        folderBox = new JCheckBox("useFolder", data.useFolder);
        setMargin(folderBox, 5, 10);
        file.add(folderBox);

        //use prefix
        prefixBox = new JCheckBox("usePrefix", data.usePrefix);
        setMargin(prefixBox, 5, 10);
        file.add(prefixBox);

        //auto dispose
        disposeBox = new JCheckBox("autoDispose", data.autoDispose);
        setMargin(disposeBox, 5, 10);
        file.add(disposeBox);

        container.add(file);
        setDivision(container);
    }


    /**
     * Generate file name and button
     */
    private void setNameAndConfirm(Container container) {
        JPanel nameField = new JPanel();
        nameField.setLayout(new FlowLayout());
        nameField.setBorder(BorderFactory.createTitledBorder("Module Name"));
        nameTextField = new JTextField(30);
        nameTextField.addKeyListener(keyListener);
        nameField.add(nameTextField);
        container.add(nameField);

        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());

        //Set bottom spacing
        setDivision(container);

        //OK cancel button
        JButton cancel = new JButton("Cancel");
        cancel.setForeground(JBColor.RED);
        cancel.addActionListener(actionListener);

        JButton ok = new JButton("OK");
        ok.setForeground(JBColor.GREEN);
        ok.addActionListener(actionListener);
        menu.add(cancel);
        menu.add(ok);
        container.add(menu);
    }

    private void createFile() {
        String type = templateGroup.getSelection().getActionCommand();
        //deal default value
        if ("Default".equals(type)) {
            data.defaultMode = true;
        } else if ("Easy".equals(type)) {
            data.defaultMode = false;
        }
        data.useFolder = folderBox.isSelected();
        data.usePrefix = prefixBox.isSelected();
        data.autoDispose = disposeBox.isSelected();


        String name = nameTextField.getText();
        String prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        String folder = "";
        String prefixName = "";

        //Add folder
        if (data.useFolder) {
            folder = "/" + prefix;
        }

        //Add prefix
        if (data.usePrefix) {
            prefixName = prefix + "_";
        }

        switch (type) {
            case "Default":
                generateDefault(folder, prefixName);
                break;
            case "Easy":
                generateEasy(folder, prefixName);
                break;
        }
    }

    private void generateDefault(String folder, String prefixName) {
        String path = psiPath + folder;
        generateFile("state.dart", path, prefixName + data.stateName.toLowerCase() + ".dart");
        generateFile("logic.dart", path, prefixName + data.logicName.toLowerCase() + ".dart");
        generateFile("view.dart", path, prefixName + data.viewFileName.toLowerCase() + ".dart");
    }

    private void generateEasy(String folder, String prefixName) {
        String path = psiPath + folder;
        generateFile("easy/logic.dart", path, prefixName + data.logicName.toLowerCase() + ".dart");
        generateFile("easy/view.dart", path, prefixName + data.viewFileName.toLowerCase() + ".dart");
    }


    private void generateFile(String inputFileName, String filePath, String outFileName) {
        //content deal
        String content = dealContent(inputFileName, outFileName);

        //Write file
        try {
            File folder = new File(filePath);
            // if file doesnt exists, then create it
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(filePath + "/" + outFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //content need deal
    private String dealContent(String inputFileName, String outFileName) {
        //deal auto dispose
        String autoDispose = "";
        if (data.autoDispose) {
            autoDispose = "auto/";
        }

        //read file
        String content = "";
        try {
            InputStream in = this.getClass().getResourceAsStream("/templates/" + autoDispose + inputFileName);
            content = new String(readStream(in));
        } catch (Exception e) {
        }


        String prefixName = "";
        //Adding a prefix requires modifying the imported class name
        if (data.usePrefix) {
            prefixName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, nameTextField.getText()) + "_";
        }
        content = content.replaceAll("logic.dart", prefixName + data.logicName.toLowerCase() + ".dart");
        content = content.replaceAll("state.dart", prefixName + data.stateName.toLowerCase() + ".dart");
        //replace logic
        if (outFileName.contains(data.logicName.toLowerCase())) {
            content = content.replaceAll("Logic", data.logicName);
            content = content.replaceAll("State", data.stateName);
            content = content.replaceAll("state", data.stateName.toLowerCase());
        }
        //replace state
        if (outFileName.contains(data.stateName.toLowerCase())) {
            content = content.replaceAll("State", data.stateName);
        }
        //replace view
        if (outFileName.contains(data.viewFileName.toLowerCase())) {
            content = content.replaceAll("Page", data.viewName);
            content = content.replaceAll("Logic", data.logicName);
            content = content.replaceAll("logic", data.logicName.toLowerCase());
            content = content.replaceAll("\\$nameState", "\\$name" + data.stateName);
            content = content.replaceAll("state", data.stateName.toLowerCase());
        }


        content = content.replaceAll("\\$name", nameTextField.getText());

        return content;
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }


    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) save();
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) dispose();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Cancel")) {
                dispose();
            } else {
                save();
            }
        }
    };

    private void setPadding(JRadioButton btn, int top, int bottom) {
        btn.setBorder(BorderFactory.createEmptyBorder(top, 10, bottom, 0));
    }

    private void setMargin(JCheckBox btn, int top, int bottom) {
        btn.setBorder(BorderFactory.createEmptyBorder(top, 10, bottom, 0));
    }

    private void setDivision(Container container) {
        //Separate the spacing between modules
        JPanel margin = new JPanel();
        container.add(margin);
    }

    private void dispose() {
        jDialog.dispose();
    }
}
