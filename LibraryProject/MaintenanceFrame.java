import javax.swing.*;
import java.awt.*;

public class MaintenanceFrame extends JFrame {

    public MaintenanceFrame(String username, boolean isAdmin) {
        setTitle("Maintenance");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());

        // nav buttons at the top
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(homeBtn);
        nav.add(logoutBtn);
        main.add(nav, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 3, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        center.add(new JLabel("Membership:", SwingConstants.RIGHT));
        JButton addMemBtn = new JButton("Add");
        JButton updateMemBtn = new JButton("Update");
        center.add(addMemBtn);
        center.add(updateMemBtn);

        center.add(new JLabel("Books/Movies:", SwingConstants.RIGHT));
        JButton addBookBtn = new JButton("Add");
        JButton updateBookBtn = new JButton("Update");
        center.add(addBookBtn);
        center.add(updateBookBtn);

        center.add(new JLabel("User Management:", SwingConstants.RIGHT));
        JButton addUserBtn = new JButton("Add/Update");
        center.add(addUserBtn);
        center.add(new JLabel(""));

        center.add(new JLabel(""));
        center.add(new JLabel(""));
        center.add(new JLabel(""));

        main.add(center, BorderLayout.CENTER);

        addMemBtn.addActionListener(e -> {
            dispose();
            new AddMembershipFrame(username, isAdmin).setVisible(true);
        });

        updateMemBtn.addActionListener(e -> {
            dispose();
            new UpdateMembershipFrame(username, isAdmin).setVisible(true);
        });

        addBookBtn.addActionListener(e -> {
            dispose();
            new AddBookFrame(username, isAdmin).setVisible(true);
        });

        updateBookBtn.addActionListener(e -> {
            dispose();
            new UpdateBookFrame(username, isAdmin).setVisible(true);
        });

        addUserBtn.addActionListener(e -> {
            dispose();
            new UserManagementFrame(username, isAdmin).setVisible(true);
        });

        homeBtn.addActionListener(e -> {
            dispose();
            new AdminHomeFrame(username).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(main);
    }
}