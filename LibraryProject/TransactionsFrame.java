import javax.swing.*;
import java.awt.*;

public class TransactionsFrame extends JFrame {

    public TransactionsFrame(String username, boolean isAdmin) {
        setTitle("Transactions");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(homeBtn);
        nav.add(logoutBtn);
        main.add(nav, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton availBtn = new JButton("Is book available?");
        JButton issueBtn = new JButton("Issue book?");
        JButton returnBtn = new JButton("Return book?");
        JButton fineBtn = new JButton("Pay Fine?");

        center.add(availBtn);
        center.add(issueBtn);
        center.add(returnBtn);
        center.add(fineBtn);
        main.add(center, BorderLayout.CENTER);

        availBtn.addActionListener(e -> {
            dispose();
            new BookAvailableFrame(username, isAdmin).setVisible(true);
        });

        issueBtn.addActionListener(e -> {
            dispose();
            new BookIssueFrame(username, isAdmin).setVisible(true);
        });

        returnBtn.addActionListener(e -> {
            dispose();
            new ReturnBookFrame(username, isAdmin).setVisible(true);
        });

        fineBtn.addActionListener(e -> {
            dispose();
            new PayFineFrame(username, isAdmin).setVisible(true);
        });

        homeBtn.addActionListener(e -> {
            dispose();
            if (isAdmin) {
                new AdminHomeFrame(username).setVisible(true);
            } else {
                new UserHomeFrame(username).setVisible(true);
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(main);
    }
}