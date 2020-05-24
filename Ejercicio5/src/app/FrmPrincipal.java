package app;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FrmPrincipal
 */
public class FrmPrincipal extends JFrame implements ActionListener {

    private ArrayList<String> collection;
    private JTextField col1;
    private JTextField col2;
    private JTextField col3;
    private JTextField lines;
    private JLabel lblCol1;
    private JLabel lblCol2;
    private JLabel lblCol3;
    private JLabel lblLines;
    private JLabel message;
    private JButton btnFind;
    private JButton btnMkFile;

    public FrmPrincipal() {

        super();
        this.setLayout(null);

        collection = new ArrayList<>();

        lblCol1 = new JLabel("Col 1");
        lblCol1.setSize(50, lblCol1.getPreferredSize().height);
        lblCol1.setLocation(30, 15);
        this.add(lblCol1);

        lblCol2 = new JLabel("Col 2");
        lblCol2.setSize(50, lblCol2.getPreferredSize().height);
        lblCol2.setLocation(105, 15);
        this.add(lblCol2);

        lblCol3 = new JLabel("Col 3");
        lblCol3.setSize(50, lblCol3.getPreferredSize().height);
        lblCol3.setLocation(180, 15);
        this.add(lblCol3);

        lblLines = new JLabel("Número de líneas:");
        lblLines.setSize(120, lblLines.getPreferredSize().height);
        lblLines.setLocation(30, 130);
        this.add(lblLines);

        message = new JLabel("..................");
        message.setSize(220, message.getPreferredSize().height);
        message.setLocation(10, 220);
        message.setVisible(false);
        this.add(message);

        col1 = new JTextField();
        col1.setSize(70, col1.getPreferredSize().height);
        col1.setLocation(10, 40);
        col1.setEnabled(false);
        this.add(col1);

        col2 = new JTextField();
        col2.setSize(70, col2.getPreferredSize().height);
        col2.setLocation(85, 40);
        col2.setEnabled(false);
        this.add(col2);

        col3 = new JTextField();
        col3.setSize(70, col3.getPreferredSize().height);
        col3.setLocation(160, 40);
        col3.setEnabled(false);
        this.add(col3);

        lines = new JTextField();
        lines.setSize(50, lines.getPreferredSize().height);
        lines.setLocation(165, 130);
        this.add(lines);

        btnFind = new JButton("Buscar");
        btnFind.setSize(220, btnFind.getPreferredSize().height);
        btnFind.setLocation(10, 75);
        btnFind.addActionListener(this);
        this.add(btnFind);

        btnMkFile = new JButton("Crear CSV");
        btnMkFile.setSize(220, btnMkFile.getPreferredSize().height);
        btnMkFile.setLocation(10, 175);
        btnMkFile.addActionListener(this);
        this.add(btnMkFile);
    }

    public void setCollection(File f) throws CsvException {

        this.collection.clear();
        String[] text;

        try (Scanner s = new Scanner(f)) {

            while (s.hasNext()) {
                text = s.nextLine().split(",");

                if (text.length == 3) {
                    for (int i = 0; i < text.length; i++) {
                        this.collection.add(text[i]);
                    }
                } else {
                    throw new CsvException("El formato del archivo no es correcto.");
                }
            }
        } catch (IOException exc) {
            System.err.println("Error de acceso al archivo: " + exc.getMessage());
        }
    }

    public ArrayList<String> getCollection() {

        return this.collection;
    }

    public double average(int col) {
        int cont = 0;
        double sum = 0;
        for (int i = 0; i < this.collection.size(); i++) {
            if (i == col) {
                sum = sum + Double.parseDouble(this.collection.get(i));
                col += 3;
                cont++;
            }
        }
        return sum / cont;
    }

    public void createCSV(int lines) {
        File f = null;
        int n1;
        double n2;
        double n3;

        try {
            f = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "numbers.txt");
            if (!f.exists()) {
                f.createNewFile();

                try (PrintWriter p = new PrintWriter(new FileWriter(f, true))) {
                    for (int i = 0; i < lines; i++) {
                        n1 = (int) (Math.random() * 101) + 9;
                        n2 = (Math.random() * 1000);
                        n3 = (Math.random() * 10000) + 100;
                        p.printf("%d,%f,%f\n", n1, n2, n3);
                    }
                    p.close();

                } catch (IOException exec) {
                    System.err.println("Error de acceso al archivo");
                }

                message.setText("Se ha creado con éxito");
                message.setVisible(true);
            }

        } catch (Exception e) {
            message.setText("El archivo no se ha creado");
            message.setVisible(true);
        }
    }

    public String checkDouble(double n) {
        String num = Double.toString(n);
        String aux = "";

        for (int i = 0; i < num.length(); i++) {

            if (num.charAt(i) == ',') {
                aux += '.';
            } else {
                aux += num.charAt(i);
            }
        }
        return aux;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnFind) {
            int answer;
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Texto", "txt");
            fc.addChoosableFileFilter(textFilter);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            answer = fc.showOpenDialog(this);

            if (answer == JFileChooser.APPROVE_OPTION) {
                try {
                    setCollection(fc.getSelectedFile());
                    col1.setText(String.format("%d", (int) average(0)));
                    col2.setText(checkDouble(average(1)));
                    col3.setText(checkDouble(average(2)));
                } catch (Exception exc) {
                    col1.setText(":c");
                    col2.setText(":c");
                    col3.setText(":c");
                    message.setText(exc.getMessage());
                    message.setVisible(true);
                }
            }
        }

        if (e.getSource() == btnMkFile) {
            try {
                createCSV(Integer.parseInt(lines.getText()));
            } catch (NumberFormatException exc) {
                message.setText("Introduce un número entero.");
                message.setVisible(true);
            }
        }
    }
}