public class TestDAO {
    public static void main(String[] args) {

        LostItemDAO.insertItem(
                "ID Card",
                "Student ID near lab",
                "Central Block",
                true
        );

        System.out.println("---- ALL ITEMS ----");
        LostItemDAO.getAllItems().forEach(System.out::println);

        System.out.println("---- HIGH PRIORITY ----");
        LostItemDAO.getHighPriorityItems().forEach(System.out::println);
    }
}
