import java.util.ArrayList;

class ZoneRepository<T extends Item> {     //ZoneRepository my generic class, doesnt know what type yet hence T, 
                                        // but it should be an Item instance ie the base class

    private ArrayList<T> items = new ArrayList<>();    //items stores only one type, either small, general

    void addItem(T item) {   //accepts only same type object, type safety
        items.add(item);
    }

    ArrayList<T> getItems() {
        return items;
    }

    void displayItems() {
        for (T item : items) { //print items list, already toString() is there in Item base class
            System.out.println(item);
        }
    }
}
