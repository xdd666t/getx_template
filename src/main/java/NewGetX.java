import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import helper.DataService;
import org.bouncycastle.asn1.dvcs.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Locale;


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
    private JCheckBox folderBox, prefixBox;

    /**
     * @param event
     */
    private String canModifyLogic;

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

        //can modify name
        canModifyLogic = data.logicName;
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
     * Set the overall pop-up style
     */
    private void setJDialog() {
        //The focus is on the current pop-up window,
        // and the focus will not shift even if you click on other areas
        jDialog.setModal(true);
        //Set padding
        ((JPanel) jDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jDialog.setSize(400, 320);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    /**
     * Main module
     *
     * @param container
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
     *
     * @param container
     */
    private void setCodeFile(Container container) {
        //Select build file
        JPanel file = new JPanel();
        file.setLayout(new GridLayout(1, 2));
        file.setBorder(BorderFactory.createTitledBorder("Select Function"));

        //Use folder
        folderBox = new JCheckBox("useFolder", data.useFolder);
        setMargin(folderBox, 5, 10);
        file.add(folderBox);

        //Use prefix
        prefixBox = new JCheckBox("usePrefix", data.usePrefix);
        setMargin(prefixBox, 5, 10);
        file.add(prefixBox);

        container.add(file);
        setDivision(container);
    }


    /**
     * Generate file name and button
     *
     * @param container
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

    private void createFile() {
        String type = templateGroup.getSelection().getActionCommand();
        String name = nameTextField.getText();
        String prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        String folder = "";
        String prefixName = "";

        //Add folder
        if (folderBox.isSelected()) {
            folder = "/" + prefix;
        }

        //Add prefix
        if (prefixBox.isSelected()) {
            prefixName = prefix + "_";
        }

        switch (type) {
            case "Default":
                data.defaultMode = true;
                generateDefault(folder, prefixName);
                break;
            case "Easy":
                data.defaultMode = false;
                generateEasy(folder, prefixName);
                break;
        }
        //deal folder and folder value
        data.useFolder = folderBox.isSelected();
        data.usePrefix = prefixBox.isSelected();
    }

    private void generateDefault(String folder, String prefixName) {
        String path = psiPath + folder;
        generateFile("state.dart", path, prefixName + "state.dart");
        generateFile("logic.dart", path, prefixName + canModifyLogic.toLowerCase() + ".dart");
        generateFile("view.dart", path, prefixName + "view.dart");
    }

    private void generateEasy(String folder, String prefixName) {
        String path = psiPath + folder;
        generateFile("easy/logic.dart", path, prefixName + canModifyLogic.toLowerCase() + ".dart");
        generateFile("easy/view.dart", path, prefixName + "view.dart");
    }


    private void generateFile(String inputFileName, String filePath, String outFileName) {
        //Get file content
        String content = "";
        try {
            InputStream in = this.getClass().getResourceAsStream("/templates/" + inputFileName);
            content = new String(readStream(in));
        } catch (Exception e) {
        }
        content = content.replaceAll("\\$name", nameTextField.getText());
        //Adding a prefix requires modifying the imported class name
        if (prefixBox.isSelected()) {
            content = content.replaceAll("logic.dart", outFileName);
            content = content.replaceAll("state.dart", outFileName);
        }
        //replace logic name
        content = content.replaceAll("Logic", canModifyLogic);

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
