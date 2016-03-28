import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class MainView extends JFrame implements ActionListener {

    private static final Logger log = Logger.getLogger(MainView.class);
    private final JEditorPane area = new JEditorPane("text/html", "");
    private JTextField firstName;
    private JTextField middleName;
    private JTextField lastName;
    private JFormattedTextField dateOfBirth;
    private JTextField phone;
    private JTextField skills;
    private JTextField languages;
    private JButton setButton;
    private JButton clearButton;
    private JTextArea aboutArea;

    private File file;
    CVParser parser;
    public MainView(){
        super("Curriculum vitae");
        parser = new CVParser();
        setLayout(new BorderLayout());
        setLookAndFeel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

        aboutArea = new JTextArea();
        firstName = new JTextField();
        firstName.addActionListener(this);
        middleName = new JTextField();
        middleName.addActionListener(this);
        lastName = new JTextField();
        lastName.addActionListener(this);
        dateOfBirth = new JFormattedTextField(java.util.Calendar.getInstance().getTime());
        dateOfBirth.addActionListener(this);
        phone = new JTextField();
        phone.addActionListener(this);
        languages = new JTextField();
        languages.addActionListener(this);
        skills = new JTextField();
        skills.addActionListener(this);

        JLabel firstNameLabel = new JLabel("First name");
        firstNameLabel.setLabelFor(firstName);
        JLabel middleNameLabel = new JLabel("Middle name");
        firstNameLabel.setLabelFor(middleName);
        JLabel lastNameLabel = new JLabel("Last name");
        firstNameLabel.setLabelFor(lastName);
        JLabel phoneLabel = new JLabel("Phone number");
        firstNameLabel.setLabelFor(phone);
        JLabel dateOfBirthLabel = new JLabel("Date of Birth");
        firstNameLabel.setLabelFor(dateOfBirth);
        JLabel languagesLabel = new JLabel("Languages");
        languagesLabel.setLabelFor(languages);
        JLabel skillsLabel = new JLabel("Skills");
        skillsLabel.setLabelFor(skills);

        JPanel controlPane = new JPanel();
        GridBagLayout bagLayout = new GridBagLayout();
        GridBagConstraints c =  new GridBagConstraints();

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;

        controlPane.setLayout(bagLayout);

        JLabel[] labels = { firstNameLabel, middleNameLabel, lastNameLabel
                           , phoneLabel, dateOfBirthLabel, languagesLabel, skillsLabel };
        JTextField[] fields = { firstName, middleName, lastName
                               ,phone, dateOfBirth, languages, skills};

        addLabelTextRows(labels, fields, bagLayout, controlPane);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;

        setButton = new JButton("Set");
        setButton.setActionCommand("setButton");
        setButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.setActionCommand("clearButton");
        clearButton.addActionListener(this);

        controlPane.add(setButton);
        controlPane.add(clearButton,c );

        controlPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Text fields"),
                BorderFactory.createEmptyBorder(5,5,5,5)
        ));


        area.setFont(new Font("Courier New", Font.ITALIC, 14));
        area.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createCompoundBorder());
        scrollPane.setPreferredSize(new Dimension(250,250));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Vitae"),
                        BorderFactory.createEmptyBorder(5,5,5,5)),
                scrollPane.getBorder()
        ));
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel persPanel = new JPanel(new BorderLayout());
        persPanel.add(controlPane, BorderLayout.PAGE_START);
        persPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.add("Personal info",persPanel);
        add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openFile = new JMenuItem("Open");
        openFile.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openFile.setActionCommand("openFile");
        openFile.addActionListener(this);
        menu.add(openFile);

        final JMenuItem saveFile = new JMenuItem("Save");
        saveFile.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveFile.setActionCommand("saveFile");
        saveFile.addActionListener(this);
        menu.add(saveFile);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void addLabelTextRows(JLabel[] labels,
                                  JTextField[] textFields,
                                  GridBagLayout gridbag,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("setButton")){
            area.setText(formatCV(false));
        }
        if(e.getActionCommand().equals("clearButton"))
            area.setText("");
        if(e.getActionCommand().equals("openFile")){
            JFileChooser fileChooser = new JFileChooser();
            int val = fileChooser.showOpenDialog(MainView.this);
            if(val == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
                area.setText(readFromFile(file));
                log.info("Opening: " + file.getName());
            }
        }
        if(e.getActionCommand().equals("saveFile")){
            JFileChooser fileChooser = new JFileChooser();
            int val = fileChooser.showSaveDialog(MainView.this);
            if(val == JFileChooser.APPROVE_OPTION){
                try(FileWriter fw = new FileWriter(fileChooser.getSelectedFile() + ".txt")){
                    fw.write(area.getText());
                } catch (IOException e1) {
                    log.error("IOException in MainWindow: " + e1);
                }
                log.info("Save as file: " + fileChooser.getSelectedFile().getName());
            }
        }
    }

    private String formatCV(boolean f){
        String lastName = "";
        String middleName = "" ;
        String firstName = "";
        String phone = "";
        String dateOfBirth = "";
        StringBuilder cvText = new StringBuilder();
        String languages = "";
        String skills = "";
        if(f){
            firstName = parser.getFirstName();
            middleName = parser.getMiddleName();
            lastName = parser.getLastName();
            phone = parser.getPhone();
            dateOfBirth = parser.getDateOfBirth();
            languages = parser.getLanguages();
            skills = parser.getSkills();
        }else{
            firstName = this.firstName.getText();
            middleName = this.middleName.getText();
            lastName = this.lastName.getText();
            phone = this.phone.getText();
        }
        cvText.append("<h1 align=\"center\"><b>" + firstName + " " + middleName + " " + lastName + "</b></h1>" );
        cvText.append("<b>Birth day: </b>" + dateOfBirth);
        cvText.append("<br><b>Phone: </b></br>" + phone);
        cvText.append("<br><b>Languages: </b></br>" + languages);
        cvText.append("<br><b>Skills: </b></br>" + skills);



        return cvText.toString();
    }

    private String readFromFile(File file){
        if(file == null)
            throw new IllegalArgumentException("Incorrect file");
        parser.parse(file.getAbsolutePath());
        return formatCV(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainView();
            }
        });
    }
}
