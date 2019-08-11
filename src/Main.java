import javax.swing.*;
import java.awt.*;

public class Main {
    static final int WIDTH = 1280;
    static final int HEIGHT = 9 * WIDTH / 16;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Visual");
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.add(new VisualPanel(), BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
