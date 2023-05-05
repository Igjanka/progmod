import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class Main {

    //private static final Scanner be = new Scanner(System.in);
    private static final String file = "src/main/resources/grades.xml";
    private static final JFrame frame = new JFrame("Average and KKI calculator");

    public static void main(String[] args) {
        ArrayList<Grades> grades = read(file, frame);

        /**
         * Panel setup
         */
        JPanel panel = new JPanel();
        panel.setBounds(new Rectangle());
        panel.setBackground(Color.LIGHT_GRAY);

        /**
         * Assign buttons and their action listeners, each with different actions
         */
        JButton button1 = new JButton("List Subjects and Grades");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessageDialog(frame, grades);
            }
        });

        JButton button2 = new JButton("Add new subject");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Grades newgrade = new Grades(inputSub(), inputCrd(), inputGrd());
                grades.add(newgrade);
            }
        });

        JButton button3 = new JButton("Modify subject");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifygrades(frame, grades);
            }
        });

        JButton button4 = new JButton("Delete subject");
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGrade(frame, grades);
            }
        });

        JButton button5 = new JButton("Average Calculator");
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avg(frame);
            }
        });

        JButton button6 = new JButton("KKI Calculator");
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kkiszam(frame);
            }
        });

        JButton button7 = new JButton("Exit");
        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGradestoXML(grades, file);
                System.exit(0);
            }
        });

        /**
         * Setting button backgrounds
         */
        button1.setBackground(Color.WHITE);
        button2.setBackground(Color.WHITE);
        button3.setBackground(Color.WHITE);
        button4.setBackground(Color.WHITE);
        button5.setBackground(Color.WHITE);
        button6.setBackground(Color.WHITE);
        button7.setBackground(Color.WHITE);

/*
        button1.setBounds(50,100,50,30);
        button2.setBounds(100,100,50,30);
        button3.setBounds(150,100,50,30);
        button4.setBounds(200,100,50,30);
        button5.setBounds(250,100,50,30);
        button6.setBounds(300,100,50,30);
        button7.setBounds(350,100,50,30);
*/

        /**
         * Add buttons to the panel, which is later added to the JFrame
         */
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        panel.add(button6);
        panel.add(button7);

        /**
         * Setting up JMenuBar, adding different items and sub lists
         */
        JMenuBar mb = new JMenuBar();
        JMenu file0 = new JMenu("Fájl");

        JMenuItem exit,save,open,add, modify, list, kki, atlag, delete;
        JMenu out  = new JMenu("Metódusok");

        mb.add(file0);
        mb.add(out);
        mb.add(exit=new JMenuItem("Exit"));

        out.add(list = new JMenuItem("List Subjects and Grades"));
        //out.add(addmodify = new JMenuItem("Add or Modify"));
        JMenu addmodify = new JMenu("Add or Modify");
        out.add(addmodify);

        addmodify.add(add = new JMenuItem("Add new subject"));
        addmodify.add(modify = new JMenuItem("Modify subject"));
        addmodify.add(delete = new JMenuItem("Delete subject"));

        out.add(atlag = new JMenuItem("Average Calculator"));
        out.add(kki = new JMenuItem("KKI Calculator"));

        file0.add(open=new JMenuItem("Open"));
        file0.add(save=new JMenuItem("Save"));

        mb.add(new JMenuItem(""));
        mb.add(new JMenuItem(""));
        mb.add(new JMenuItem(""));

        /**
         * Adding the action listeners to MenuItems, each with it's purposed function
         */
        open.addActionListener(e -> {
            showMessageDialog(frame, "Read completed in the beginning.");
        });

        save.addActionListener(e ->{
            saveGradestoXML(grades, file);
        });

        list.addActionListener(e->{
            showMessageDialog(frame, grades);
        });

        add.addActionListener(e->{
            grades.add(new Grades(inputSub(), inputCrd(), inputGrd()));
        });

        modify.addActionListener(e->{
            modifygrades(frame, grades);
        });

        atlag.addActionListener(e->{
            avg(frame);
        });

        kki.addActionListener(e->{
            kkiszam(frame);
        });

        delete.addActionListener(e->{
            deleteGrade(frame, grades);
        });

        exit.addActionListener(e ->{
            saveGradestoXML(grades, file);
            System.exit(0);
        });

        /**
         * Final touches on the frame, adding the
         * forementioned panel, menubar, and smaller adjustments
         */
        frame.add(panel);
        frame.setJMenuBar(mb);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    /**
     * Part of the program, which calculates the KKI
     * @param frame
     */
    private static void kkiszam(JFrame frame) {
        float kki, divider = 30;
        kki = (sumOfDoneTimesGrade()/divider)*(DoneCrd()/UnderTook());
        showMessageDialog(frame, "Your KKI based on Grades.xml is: " + kki);
    }

    /**
     * Returns the value of the credits you undertook, float in order,
     * so later it can make more accurate calculations in kkiszam
     * @return
     */
    private static float UnderTook() {
        float undertook = 0;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            if (childNodeOfGradesTag.getNodeName().equals("Credit")) {
                                crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    undertook = undertook + Integer.parseInt(crd);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return undertook;
    }

    /**
     * Returns the value of the credits you completed, float in order,
     * so later it can make more accurate calculations in kkiszam
     * @return
     */
    private static float DoneCrd() {
        float done = 0;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="", crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfGradesTag.getNodeName()) {
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }

                    if(Integer.parseInt(grd) != 1) {
                        done = done + Integer.parseInt(crd);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    /**
     * Returns the sum value the credits times grade, float in order,
     * so later it can make more accurate calculations in kkiszam
     * @return
     */
    private static float sumOfDoneTimesGrade() {
        float sum = 0;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="", crd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfGradesTag.getNodeName()){
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }

                    sum = sum + Integer.parseInt(grd)*Integer.parseInt(crd);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * The part of the program which modifies the given subject's
     * credit and grade, uses multiple sub-programs
     * @param frame
     * @param grades
     */
    private static void modifygrades(JFrame frame, ArrayList<Grades> grades) {
        String subj = JOptionPane.showInputDialog("Subject you want to modify: ");
        try {
            Grades grades1 = findGrade(grades, subj);
            grades.set(grades.indexOf(grades1), new Grades(grades1.getSubject(), inputCrd(), inputGrd()));
            showMessageDialog(frame, "Subject changed.");
        } catch (IllegalArgumentException e) {
            showMessageDialog(frame, e.getMessage());
        }
    }

    /**
     * The part of the program which deletes the given subject
     * uses multiple sub-programs
     * @param frame
     * @param grades
     */
    private static void deleteGrade(JFrame frame, ArrayList<Grades> grades) {
        String name = JOptionPane.showInputDialog("Subject you want to delete: ");
        try {
            grades.remove(findGrade(grades, name));
            showMessageDialog(frame, "Subject deleted.");
        }
        catch (IllegalArgumentException e) {
            showMessageDialog(frame, e.getMessage());
        }
    }

    /**
     * Smaller program part, finds the given subject in the ArrayList,
     * it's used in delete and modify programs
     * @param grade
     * @param sub
     * @return
     * @throws IllegalArgumentException
     */
    private static Grades findGrade(ArrayList<Grades> grade, String sub) throws IllegalArgumentException {
        for(Grades grades : grade) {
            if(grades.getSubject().equals(sub)) {
                return grades;
            }
        }
        throw new IllegalArgumentException("No subject with given name: " + sub);
    }

    /**
     * Counts the average of the Grades.xml,
     * uses 2 subprograms
     * @param frame
     */
    private static void avg(JFrame frame) {
        float avg = gradesSum() / gradesNo();
        showMessageDialog(frame, "Average of the grades: " + avg);
    }

    /**
     * Returns the sum of grades, one of the subprogram of avg
     * @return
     */
    private static float gradesSum() {
        float sum = 0;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String grd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            if (childNodeOfGradesTag.getNodeName().equals("Grade")) {
                                grd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    sum = sum + Integer.parseInt(grd);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * Returns the number of grades, other subprogram of avg
     * @return
     */
    private static float gradesNo() {
        float No = 0;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            if (childNodeOfGradesTag.getNodeName().equals("Grade")) {
                                No++;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return No;
    }

    /**
     * Reads the contents of grades.xml and returns and ArrayList
     * which is later used by the other parts of the program
     * @param file0
     * @param frame
     * @return
     */
    private static ArrayList<Grades> read(String file0, JFrame frame) {
        ArrayList<Grades> grade = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file0);

            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;

            for(int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfGradesTag = node.getChildNodes();
                    String sub = "", crd = "", grd ="";
                    for(int j = 0; j < childNodesOfGradesTag.getLength(); j++) {
                        Node childNodeOfGradesTag = childNodesOfGradesTag.item(j);
                        if(childNodeOfGradesTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfGradesTag.getNodeName()){
                                case "Subject" -> sub = childNodeOfGradesTag.getTextContent();
                                case "Credit" -> crd = childNodeOfGradesTag.getTextContent();
                                case "Grade" -> grd = childNodeOfGradesTag.getTextContent();
                            }
                        }
                    }
                    grade.add(new Grades(sub, Integer.parseInt(crd), Integer.parseInt(grd)));
                    //out.println(grade);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //showMessageDialog(frame, grade);
        //showMessageDialog(frame,"Read complete!");
        return grade;
    }

    /**
     * Saves the changes of the ArrayList into grades.xml,
     * uses the childElement
     * @param grade
     * @param filepath1
     */
    private static void saveGradestoXML(ArrayList<Grades> grade, String filepath1) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("Semester");
            document.appendChild(root);
            for(Grades grades : grade) {
                Element gradeElement = document.createElement("Sub");
                root.appendChild(gradeElement);
                childElement(document, gradeElement, "Subject", grades.getSubject());
                childElement(document, gradeElement, "Credit", String.valueOf(grades.getCredit()));
                childElement(document, gradeElement, "Grade", String.valueOf(grades.getGrade()));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(filepath1));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates childElements for saveGradestoXML
     * @param document
     * @param par
     * @param tag
     * @param text
     */
    private static void childElement(Document document, Element par, String tag, String text) {
        Element element = document.createElement(tag);
        element.setTextContent(text);
        par.appendChild(element);
    }

    /**
     * Subprogram of add new and modify, catches the wrong inputs, adds grade
     * @return
     */
    private static int inputGrd() {
        int grd = 0;
        try {
            while (grd < 1 || grd > 5) {
                grd = Integer.parseInt(JOptionPane.showInputDialog("Enter the grade of the new subject: "));
                try {
                    if (grd > 5 || grd < 1) {
                        showMessageDialog(frame, "Grade must be between 1 and 5");
                    }
                } catch (InputMismatchException e) {
                    showMessageDialog(frame, "Grade must be a number");
                }
            }
        }
        catch (Exception e){
            showMessageDialog(frame, e.getMessage());
        }
        return grd;
    }

    /**
     * Subprogram of add new and modify, catches the wrong inputs, adds credit
     * @return
     */
    private static int inputCrd() {
        int crd = 0;
        try {
            crd = Integer.parseInt(JOptionPane.showInputDialog("Enter credit value of new subject: "));
            while (crd <= 0 || crd > 9) {
                crd = Integer.parseInt(JOptionPane.showInputDialog("Credit value invalid. Please reenter the value"));
            }
        }
        catch (Exception e){
            showMessageDialog(frame, e.getMessage());
        }
        return crd;
    }

    /**
     * Subprogram of add new
     * @return
     */
    private static String inputSub() {
        String sub = new String();
        try{
            sub = JOptionPane.showInputDialog("Name of new subject: ");

        }
        catch (Exception e){
            showMessageDialog(frame, e.getMessage());
        }
        return sub;
    }
}