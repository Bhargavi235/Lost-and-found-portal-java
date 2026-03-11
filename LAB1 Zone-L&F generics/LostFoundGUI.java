import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class LostFoundGUI {

    static HashMap<String, ZoneRepository<Item>> zoneMap = new HashMap<>();
    //each zone has a zone repository that stores item belonging to that zone, like block1-zonerepo 

    static Color bgColor = new Color(245, 247, 250);
    static Color primary = new Color(44, 62, 80);
    static Color accent = new Color(41, 128, 185);
    static Color danger = new Color(231, 76, 60);
    static Color success = new Color(39, 174, 96);

    public static void main(String[] args) {

        String[] zones = {
                "Block 1", "Block 2", "Block 3", "Block 4",
                "R&D Block", "Central Block", "Audi Block",
                "Birds Park Canteen", "Gourmet", "KE Cafe",
                "Ivy Hall", "Main Auditorium"
        };

        for (String z : zones) {
            zoneMap.put(z, new ZoneRepository<>());   //items dont mix between zones
        }

        JFrame frame = new JFrame("Campus Lost & Found");
        frame.setSize(520, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(bgColor);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Lost and Found Portal", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(primary);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        card.setLayout(new GridLayout(6, 1, 10, 10));

        JTextField itemName = styledTextField("Item name");
        JTextField description = styledTextField("Short description");
        JComboBox<String> zoneBox = new JComboBox<>(zones);
        zoneBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton lostButton = styledButton("Report Lost Item", danger);
        JButton foundButton = styledButton("Submit Found Item", success);

        card.add(itemName);
        card.add(description);
        card.add(zoneBox);
        card.add(lostButton);
        card.add(foundButton);

        frame.add(card, BorderLayout.CENTER);
        frame.setVisible(true);

        AlertRule<Item> alertRule = new SmallItemAlertRule();

        lostButton.addActionListener(e -> {
            Item item = new SmallItem(
                    itemName.getText(),
                    description.getText(),
                    zoneBox.getSelectedItem().toString()
            );

            zoneMap.get(item.zone).addItem(item);

            showAlert(frame,
                    "Lost Item Alert",
                    item.name + " reported in " + item.zone,
                    danger
            );

            if (alertRule.check(item)) {
                System.out.println("High priority alert: small item");
            }
        });

        foundButton.addActionListener(e -> {
            Item item = new GeneralItem(
                    itemName.getText(),
                    description.getText(),
                    zoneBox.getSelectedItem().toString()
            );

            zoneMap.get(item.zone).addItem(item);

            showAlert(frame,
                    "Item Submitted",
                    "Item submitted to " + item.zone,
                    success
            );
        });
    }

    static JTextField styledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setToolTipText(placeholder);
        return field;
    }

    static JButton styledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    static void showAlert(JFrame parent, String title, String message, Color color) {
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(color);

        JOptionPane.showMessageDialog(
                parent,
                label,
                title,
                JOptionPane.PLAIN_MESSAGE
        );
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


