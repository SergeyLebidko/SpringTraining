package springtraining;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final Font outputFont = new Font("Consolas", Font.PLAIN, 16);

    private JFrame frm;
    private JTextArea outputArea;


    private GUI() {
        frm = new JFrame("Spring Training");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(WIDTH, HEIGHT);
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT / 2;
        frm.setLocation(xPos, yPos);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        outputArea = new JTextArea();
        outputArea.setFont(outputFont);
        outputArea.setEditable(false);

        contentPane.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frm.setContentPane(contentPane);
    }

    public void showGui() {
        frm.setVisible(true);
    }

    public void print(String text) {
        outputArea.append(text + "\n");
    }

    public void print() {
        outputArea.append("\n");
    }

}
