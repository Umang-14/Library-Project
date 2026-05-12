import java.util.*;

public class DataStore {

    // username -> [password, isAdmin]
    public static Map<String, String[]> users = new HashMap<>();

    // book fields: serialNo, name, author, category, status, cost, procurementDate, type
    public static List<String[]> books = new ArrayList<>();

    // membership fields: membershipId, firstName, lastName, contact, address, aadhar, startDate, endDate, status, finePending
    public static List<String[]> memberships = new ArrayList<>();

    // currently issued books
    public static List<String[]> activeIssues = new ArrayList<>();

    // issue requests by users
    public static List<String[]> issueRequests = new ArrayList<>();

    static {
        users.put("adm", new String[]{"adm", "true"});
        users.put("user", new String[]{"user", "false"});

        // kuch sample books add kiye hain testing ke liye
        books.add(new String[]{"SCB000001", "Wings of Fire", "APJ Abdul Kalam", "Science", "Available", "220", "10/01/2023", "Book"});
        books.add(new String[]{"SCB000002", "A Brief History of Time", "Stephen Hawking", "Science", "Issued", "180", "15/02/2023", "Book"});
        books.add(new String[]{"ECB000001", "Indian Economy", "Ramesh Singh", "Economics", "Available", "350", "01/03/2023", "Book"});
        books.add(new String[]{"FCM000001", "3 Idiots", "Rajkumar Hirani", "Fiction", "Available", "499", "20/04/2023", "Movie"});
        books.add(new String[]{"CHB000001", "Goosebumps", "R.L. Stine", "Children", "Available", "120", "05/05/2023", "Book"});

        memberships.add(new String[]{"MEM001", "Arjun", "Verma", "9876543210", "Delhi", "123456789012", "01/01/2024", "31/12/2024", "Active", "0"});
        memberships.add(new String[]{"MEM002", "Sneha", "Patel", "9123456780", "Ahmedabad", "987654321098", "01/01/2024", "30/06/2024", "Active", "50"});

        activeIssues.add(new String[]{"SCB000002", "A Brief History of Time", "MEM001", "01/05/2026", "16/05/2026"});

        issueRequests.add(new String[]{"MEM002", "3 Idiots", "10/05/2026", ""});
    }

    public static boolean validateLogin(String username, String password) {
        if (users.containsKey(username)) {
            String storedPass = users.get(username)[0];
            return storedPass.equals(password);
        }
        return false;
    }

    public static boolean isAdmin(String username) {
        if (users.containsKey(username)) {
            return users.get(username)[1].equals("true");
        }
        return false;
    }

    public static String[] getBookByName(String name) {
        for (String[] b : books) {
            if (b[1].equalsIgnoreCase(name))
                return b;
        }
        return null;
    }

    public static List<String[]> searchBooks(String name, String author) {
        List<String[]> result = new ArrayList<>();
        for (String[] b : books) {
            boolean nameMatch = name.isEmpty() || b[1].toLowerCase().contains(name.toLowerCase());
            boolean authorMatch = author.isEmpty() || b[2].toLowerCase().contains(author.toLowerCase());
            if (nameMatch && authorMatch)
                result.add(b);
        }
        return result;
    }

    public static String getTodayDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return String.format("%02d/%02d/%04d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());
    }

    public static String getReturnDate() {
        // 15 din baad return karna hoga
        java.time.LocalDate ret = java.time.LocalDate.now().plusDays(15);
        return String.format("%02d/%02d/%04d", ret.getDayOfMonth(), ret.getMonthValue(), ret.getYear());
    }

    public static int calculateFine(String returnDate, String actualReturnDate) {
        try {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate ret = java.time.LocalDate.parse(returnDate, fmt);
            java.time.LocalDate actual = java.time.LocalDate.parse(actualReturnDate, fmt);
            long days = java.time.temporal.ChronoUnit.DAYS.between(ret, actual);
            // Rs 5 per day fine
            if (days > 0)
                return (int) days * 5;
        } catch (Exception e) {
            // invalid date format, fine = 0
        }
        return 0;
    }
}