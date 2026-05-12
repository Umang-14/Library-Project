import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // start karo GUI thread pe
        SwingUtilities.invokeLater(() -> {
            LoginFrame lf = new LoginFrame();
            lf.setVisible(true);
        });
    }
}