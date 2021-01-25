import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;

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
    /**
     * 整体弹窗实体
     */
    private JDialog jDialog;
    private JTextField nameTextField;
    private ButtonGroup templateGroup;
    /**
     * 多选框
     * 使用文件夹：默认true
     * 使用前缀：默认false
     */
    private JCheckBox folderBox, prefixBox;

    @Override
    public void actionPerformed(AnActionEvent event) {
        project = event.getProject();
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString();
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1);
        initView();
    }

    private void initView() {
        jDialog = new JDialog(new JFrame(), "GetX Template Code Produce");
        //设置功能按钮
        Container container = jDialog.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        //设置主模块样式: mode, function
        setModule(container);

        //设置选项: 是否使用前缀
        setCodeFile(container);

        //生成文件名称和确定取消按钮
        setNameAndConfirm(container);

        //选择弹窗样式
        setJDialog();
    }

    /**
     * 设置整体弹窗样式
     */
    private void setJDialog() {
        //焦点集中在当前弹窗,点击其它区域焦点也不会转移
        jDialog.setModal(true);
        //设置内边距
        ((JPanel) jDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jDialog.setSize(400, 320);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    /**
     * 主模块
     *
     * @param container
     */
    private void setModule(Container container) {
        //布局两行俩列
        JPanel template = new JPanel();
        template.setLayout(new GridLayout(1, 2));
        //设置主模块样式：mode, function
        template.setBorder(BorderFactory.createTitledBorder("Select Mode"));
        //default和high设置
        JRadioButton defaultBtn = new JRadioButton("Default", true);
        defaultBtn.setActionCommand("Default");
        setPadding(defaultBtn, 5, 10);
        JRadioButton highBtn = new JRadioButton("Easy");
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
     * 代码文件
     *
     * @param container
     */
    private void setCodeFile(Container container) {
        //选择生成文件
        JPanel file = new JPanel();
        file.setLayout(new GridLayout(1, 2));
        file.setBorder(BorderFactory.createTitledBorder("Select Function"));

        //使用文件夹
        folderBox = new JCheckBox("useFolder", true);
        setMargin(folderBox, 5, 10);
        file.add(folderBox);

        //使用前缀
        prefixBox = new JCheckBox("usePrefix", false);
        setMargin(prefixBox, 5, 10);
        file.add(prefixBox);

        container.add(file);
        setDivision(container);
    }


    /**
     * 生成文件名和按钮
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

        //设置底部间距
        setDivision(container);

        //确定取消按钮
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


    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                save();
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                dispose();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Cancel")) {
                dispose();
            } else {
                save();
            }
        }
    };

    private void save() {
        if (nameTextField.getText() == null || "".equals(nameTextField.getText().trim())) {
            Messages.showInfoMessage(project, "Please input the module name", "Info");
            return;
        }
        dispose();
        //创建文件
        clickCreateFile();
        //刷新项目
        project.getBaseDir().refresh(false, true);
    }

    private void clickCreateFile() {
        String type = templateGroup.getSelection().getActionCommand();
        String name = nameTextField.getText();
        String prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        String folder = "";
        String prefixName = "";

        //添加文件夹
        if (folderBox.isSelected()) {
            folder = "/" + prefix;
        }

        //添加前缀
        if (prefixBox.isSelected()) {
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
        generateFile("state.dart", path, prefixName + "state.dart");
        generateFile("logic.dart", path, prefixName + "logic.dart");
        generateFile("view.dart", path, prefixName + "view.dart");
    }

    private void generateEasy(String folder, String prefixName) {
        String path = psiPath + folder;
        generateFile("easy/logic.dart", path, prefixName + "logic.dart");
        generateFile("easy/view.dart", path, prefixName + "view.dart");
    }


    private void generateFile(String inputFileName, String filePath, String outFileName) {
        //获取文件内容
        String content = "";
        try {
            InputStream in = this.getClass().getResourceAsStream("/templates/" + inputFileName);
            content = new String(readStream(in));
        } catch (Exception e) {
        }
        content = content.replaceAll("\\$name", nameTextField.getText());

        //写入文件
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

    private void setPadding(JRadioButton btn, int top, int bottom) {
        btn.setBorder(BorderFactory.createEmptyBorder(top, 10, bottom, 0));
    }

    private void setMargin(JCheckBox btn, int top, int bottom) {
        btn.setBorder(BorderFactory.createEmptyBorder(top, 10, bottom, 0));
    }

    private void setDivision(Container container) {
        //分隔模块之间的间距
        JPanel margin = new JPanel();
        container.add(margin);
    }

    private void dispose() {
        jDialog.dispose();
    }
}
