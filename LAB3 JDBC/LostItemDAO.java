import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class LostItemDAO {

    static void insertItem(String name, String desc, String zone, boolean priority) {
        String sql = "INSERT INTO lost_items (item_name, item_desc, zone, high_priority) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setString(3, zone);
            ps.setBoolean(4, priority);

            ps.executeUpdate();
            System.out.println("Item inserted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        String sql = "SELECT * FROM lost_items";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String item =
                        rs.getInt("id") + " | " +
                        rs.getString("item_name") + " | " +
                        rs.getString("item_desc") + " | " +
                        rs.getString("zone") + " | " +
                        (rs.getBoolean("high_priority") ? "HIGH PRIORITY" : "NORMAL");

                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    static List<String> getHighPriorityItems() {
        List<String> items = new ArrayList<>();
        String sql = "SELECT * FROM lost_items WHERE high_priority = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(
                        rs.getInt("id") + " | " +
                        rs.getString("item_name") + " | " +
                        rs.getString("zone")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
