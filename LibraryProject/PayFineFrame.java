import javax.swing.*;
import java.awt.*;

public class PayFineFrame extends JFrame {

    private String username;
    private boolean isAdmin;

    public PayFineFrame(String username, boolean isAdmin) {
        this(username, isAdmin, "", "", DataStore.getTodayDate(), DataStore.getTodayDate());
    }

    public PayFineFrame(String username, boolean isAdmin, String bookName, String serial, String issueDate, String returnDate) {
        this.username = username;
        this.isAdmin = isAdmin;

        setTitle("Pay Fine");
        setSize(500, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.add(buildNav(), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Pay Fine", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;

        // fields aur unke default values
        String[] labels = {"Book Name:", "Serial No:", "Issue Date:", "Return Date:", "Actual Return Date:"};
        JTextField[] fields = new JTextField[5];
        String[] defaults = {bookName, serial, issueDate, returnDate, DataStore.getTodayDate()};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            fields[i] = new JTextField(defaults[i], 15);
            if (i < 4) {
                fields[i].setEditable(false);
                fields[i].setBackground(Color.LIGHT_GRAY);
            }
            form.add(fields[i], gbc);
        }

        gbc.gridy = 6;
        gbc.gridx = 0;
        form.add(new JLabel("Fine Calculated:"), gbc);
        gbc.gridx = 1;
        JTextField fineField = new JTextField("0");
        fineField.setEditable(false);
        form.add(fineField, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        form.add(new JLabel("Fine Paid:"), gbc);
        gbc.gridx = 1;
        JCheckBox finePaidChk = new JCheckBox();
        form.add(finePaidChk, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        form.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 1;
        JTextArea remarksArea = new JTextArea(2, 15);
        form.add(new JScrollPane(remarksArea), gbc);

        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        form.add(errorLabel, gbc);

        JButton calcBtn = new JButton("Calculate Fine");
        JButton confirmBtn = new JButton("Confirm Return");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(calcBtn);
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 10;
        form.add(btnPanel, gbc);

        calcBtn.addActionListener(e -> {
            int fine = DataStore.calculateFine(fields[3].getText(), fields[4].getText());
            fineField.setText(String.valueOf(fine));
        });

        confirmBtn.addActionListener(e -> {
            int fine = Integer.parseInt(fineField.getText());
            if (fine > 0 && !finePaidChk.isSelected()) {
                errorLabel.setText("Fine must be paid before confirming return.");
                return;
            }
            String bName = fields[0].getText();
            DataStore.activeIssues.removeIf(i -> i[1].equals(bName));
            for (String[] b : DataStore.books) {
                if (b[1].equals(bName)) {
                    b[4] = "Available";
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildNav() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton transBtn = new JButton("Transactions");
        JButton homeBtn = new JButton("Home");
        JButton logoutBtn = new JButton("Log Out");
        nav.add(transBtn);
        nav.add(homeBtn);
        nav.add(logoutBtn);
        transBtn.addActionListener(e -> {
            dispose();
            new TransactionsFrame(username, isAdmin).setVisible(true);
        });
        homeBtn.addActionListener(e -> {
            dispose();
            if (isAdmin)
                new AdminHomeFrame(username).setVisible(true);
            else
                new UserHomeFrame(username).setVisible(true);
        });
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return nav;
    }
}