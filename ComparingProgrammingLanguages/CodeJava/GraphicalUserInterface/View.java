package GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import GraphicalUserInterface.Controller;

public class View extends JFrame {

    private JPanel contentPanel;
    private JPanel inputPanel;
    private JPanel inputPanel2;

    private JLabel selectLanguage;

    private JLabel resultLabel;
    private JLabel resultValueLabel;
    private JComboBox language;

    private JButton computeButton;
    private JLabel selectOperation;
    private JComboBox operation;

    private JLabel image;
    private ImageIcon icon;

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public JLabel getImage() {
        return image;
    }

    public void setImage(JLabel image) {
        this.image = image;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getInputPanel() {
        return inputPanel;
    }

    public void setInputPanel(JPanel inputPanel) {
        this.inputPanel = inputPanel;
    }

    public JLabel getSelectLanguage() {
        return selectLanguage;
    }

    public void setSelectLanguage(JLabel selectLanguage) {
        this.selectLanguage = selectLanguage;
    }

    public JComboBox getLanguage() {
        return language;
    }

    public void setLanguage(JComboBox language) {
        this.language = language;
    }

    public JButton getComputeButton() {
        return computeButton;
    }

    public void setComputeButton(JButton computeButton) {
        this.computeButton = computeButton;
    }

    public JLabel getSelectOperation() {
        return selectOperation;
    }

    public void setSelectOperation(JLabel selectOperation) {
        this.selectOperation = selectOperation;
    }

    public JComboBox getOperation() {
        return operation;
    }

    public void setOperation(JComboBox operation) {
        this.operation = operation;
    }

    public JPanel getResultPanel() {
        return resultPanel;
    }

    public void setResultPanel(JPanel resultPanel) {
        this.resultPanel = resultPanel;
    }

    public Controller getController() {
        return controller;
    }

    public JLabel getResultLabel() {
        return resultLabel;
    }

    public void setResultLabel(JLabel resultLabel) {
        this.resultLabel = resultLabel;
    }

    public JLabel getResultValueLabel() {
        return resultValueLabel;
    }

    public void setResultValueLabel(JLabel resultValueLabel) {
        this.resultValueLabel = resultValueLabel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private JPanel resultPanel;

    Controller controller = new Controller(this);
    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    public View(String name) throws IOException {
        super(name);
        this.prepareGui();
    }
    public void prepareGui(){
        this.setSize(1500, 1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.contentPanel = new JPanel(new GridLayout(4, 2));
        this.prepareInputPanel();
        this.prepareInputPanel2();
        this.prepareResultPanel();
        this.setContentPane(this.contentPanel);
    }


    private void prepareResultPanel() {
        this.resultPanel = new JPanel();
        this.resultPanel.setLayout(new GridLayout(1,3));

        this.resultLabel = new JLabel("Measurements: ", JLabel.CENTER);
        this.resultValueLabel = new JLabel("", JLabel.CENTER);
        this.resultPanel.add(this.resultLabel);
        this.resultPanel.add(this.resultValueLabel);

        icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("test.png")));
        icon.getImage().flush();
        this.image = new JLabel();
        image.setSize(500,500);
        image.setIcon(null);
        icon.getImage().flush();
        image.setIcon(icon);
        this.resultPanel.add(this.image);

        this.contentPanel.add(this.resultPanel);
    }
    private void prepareInputPanel() {
        this.inputPanel = new JPanel();
        this.inputPanel.setLayout(new GridLayout(1, 2));
        //this.inputPanel.setSize(1500,100);

        this.selectLanguage = new JLabel("Select language", JLabel.CENTER);
        this.inputPanel.add(this.selectLanguage);

        String[] languages = new String[]{"C++", "Java", "Python"};
        this.language = new JComboBox(languages);
        this.inputPanel.add(language);

        this.selectOperation = new JLabel("Select operation", JLabel.CENTER);
        this.inputPanel.add(this.selectOperation);

        String[] operations = new String[]{"Sort an array", "Create threads", "Thread migration"};
        this.operation = new JComboBox(operations);
        this.inputPanel.add(operation);

        this.computeButton = new JButton("Compute");
        this.computeButton.setActionCommand("COMPUTE");
        this.computeButton.addActionListener(this.controller);
        this.inputPanel.add(this.computeButton);

        this.contentPanel.add(this.inputPanel);
    }
    private void prepareInputPanel2() {
        this.inputPanel2 = new JPanel();
        this.inputPanel2.setLayout(new GridLayout(1, 2));
        JLabel l1 = new JLabel(" ");
        this.inputPanel2.add(l1);
        this.contentPanel.add(this.inputPanel2);
    }
}
