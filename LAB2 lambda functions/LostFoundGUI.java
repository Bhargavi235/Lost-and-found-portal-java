import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class LostFoundGUI {

    static HashMap<String, ZoneRepository<Item>> zoneMap = new HashMap<>();

    static Color bg = new Color(245, 247, 250);
    static Color header1 = new Color(52, 152, 219);
    static Color header2 = new Color(41, 128, 185);
    static Color danger = new Color(231, 76, 60);
    static Color success = new Color(39, 174, 96);
    static Color card = Color.WHITE;

    static JLabel statusLabel = new JLabel(" No urgent lost items", JLabel.CENTER);

    public static void main(String[] args) {

        String[] zones = {
                "Block 1", "Block 2", "Block 3", "Block 4",
                "R&D Block", "Central Block", "Audi Block",
                "Birds Park Canteen", "Gourmet", "KE Cafe",
                "Ivy Hall", "Main Auditorium"
        };

        for (String z : zones) {
            zoneMap.put(z, new ZoneRepository<>());
        }

        JFrame frame = new JFrame("Campus Lost & Found Portal");
        frame.setSize(600, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bg);

        JPanel header = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, header1, 0, getHeight(), header2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(600, 70));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel(" Zone-Based Lost & Found", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        cardPanel.setBackground(card);

        JTextField itemName = new JTextField();
        JTextField description = new JTextField();
        JTextField keywordField = new JTextField();

        JCheckBox priorityBox = new JCheckBox(" Small item (high priority)");
        JComboBox<String> zoneBox = new JComboBox<>(zones);
        JComboBox<String> filterBox = new JComboBox<>(new String[]{"All Items", "High Priority Only"});

        JButton lostBtn = createButton("Report Lost Item", danger);
        JButton filterBtn = createButton("Apply Filter", header1);

        cardPanel.add(new JLabel("Item Name"));
        cardPanel.add(itemName);

        cardPanel.add(new JLabel("Description"));
        cardPanel.add(description);

        cardPanel.add(new JLabel("Zone"));
        cardPanel.add(zoneBox);

        cardPanel.add(priorityBox);
        cardPanel.add(new JLabel(""));

        cardPanel.add(new JLabel("Search Keyword"));
        cardPanel.add(keywordField);

        cardPanel.add(new JLabel("Filter View"));
        cardPanel.add(filterBox);

        cardPanel.add(lostBtn);
        cardPanel.add(filterBtn);

        frame.add(cardPanel, BorderLayout.CENTER);

        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(success);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        AlertRule<Item> priorityRule = new SmallItemAlertRule();
        Supplier<String> systemStatus = () -> "System Active";

        lostBtn.addActionListener(e -> {

            Item item = priorityBox.isSelected()
                    ? new SmallItem(itemName.getText(), description.getText(), zoneBox.getSelectedItem().toString())
                    : new GeneralItem(itemName.getText(), description.getText(), zoneBox.getSelectedItem().toString());

            zoneMap.get(item.zone).addItem(item);

            if (priorityRule.check(item)) {
                showAlert(frame,
                        " HIGH PRIORITY ALERT",
                        "Small item lost!\n\n Zone: " + item.zone + "\n Item: " + item.name +
                                "\n\n⚠ Please check desks and floors immediately.",
                        danger);
            } else {
                showAlert(frame,
                        "Item Reported",
                        item.name + "\n " + item.zone,
                        success);
            }

            List<Item> allItems = zoneMap.get(item.zone).getItems();

            if (FilterUtil.hasUrgentItem(allItems)) {
                statusLabel.setText("Urgent small item reported!");
                statusLabel.setForeground(danger);
            }
        });

        filterBtn.addActionListener(e -> {

            List<Item> items = zoneMap.get(zoneBox.getSelectedItem().toString()).getItems();

            if (!keywordField.getText().isEmpty()) {
                items = FilterUtil.filterByKeyword(items, keywordField.getText());
            }

            if (filterBox.getSelectedItem().toString().equals("High Priority Only")) {
                items = FilterUtil.filterHighPriority(items);
            }

            System.out.println("🔍 Filter Results:");
            items.forEach(System.out::println);
        });

        System.out.println(systemStatus.get());
    }

    static JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static void showAlert(JFrame parent, String title, String message, Color color) {
        JLabel label = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(color);
        JOptionPane.showMessageDialog(parent, label, title, JOptionPane.PLAIN_MESSAGE);
    }
}




// import javax.swing.*;
// import java.awt.*;
// import java.util.HashMap;

// public class LostFoundGUI {

//     static HashMap<String, ZoneRepository<Item>> zoneMap = new HashMap<>();  
//     //each zone has a zone repository that stores item belonging to that zone, like block1-zonerepo 

//     public static void main(String[] args) {

//         String[] zones = {
//                 "Block 1", "Block 2", "Block 3", "Block 4",
//                 "R&D Block", "Central Block", "Audi Block",
//                 "Birds Park Canteen", "Gourmet", "KE Cafe",
//                 "Ivy Hall", "Main Auditorium"   //array with all zones
//         };

//         for (String z : zones) {
//             zoneMap.put(z, new ZoneRepository<>());    //items dont mix between zones
//         }

//         JFrame frame = new JFrame("Campus Lost & Found Alert System");
//         frame.setSize(450, 300);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         JPanel panel = new JPanel(new GridLayout(6, 2));

//         JTextField itemName = new JTextField();
//         JTextField description = new JTextField();
//         JComboBox<String> zoneBox = new JComboBox<>(zones);

//         JButton lostButton = new JButton("Report Lost Item");
//         JButton foundButton = new JButton("Submit Found Item");

//         panel.add(new JLabel("Item Name"));
//         panel.add(itemName);

//         panel.add(new JLabel("Description"));
//         panel.add(description);

//         panel.add(new JLabel("Zone"));
//         panel.add(zoneBox);

//         panel.add(lostButton);
//         panel.add(foundButton);

//         frame.add(panel);
//         frame.setVisible(true);

//         AlertRule<Item> alertRule = new SmallItemAlertRule();    //generic interface AlertRule 

//         lostButton.addActionListener(e -> {

//             Item item = new SmallItem(   //small item class
//                     itemName.getText(),
//                     description.getText(),   
//                     zoneBox.getSelectedItem().toString()
//             );

//             zoneMap.get(item.zone).addItem(item);   //add the item to the particular zone box

//             JOptionPane.showMessageDialog(
//                     frame,
//                     "Lost Item Alert in " + item.zone + ": " + item.name
//             );

//             if (alertRule.check(item)) {    //if it returns true for small item check
//                 System.out.println("High priority alert: small item reported");
//             }     
//         });

//         foundButton.addActionListener(e -> {    

//             Item item = new GeneralItem(   // general item class
//                     itemName.getText(),
//                     description.getText(),
//                     zoneBox.getSelectedItem().toString()
//             );

//             zoneMap.get(item.zone).addItem(item);

//             JOptionPane.showMessageDialog(
//                     frame,
//                     "Found item submitted to " + item.zone
//             );
//         });
//     }
// }


