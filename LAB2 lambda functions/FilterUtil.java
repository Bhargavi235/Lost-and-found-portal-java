import java.util.List;
import java.util.stream.Collectors;   //Needed to convert a stream back into a list Streams do not store data permanently Collectors.toList() creates a new list from stream results

class FilterUtil {

    static List<Item> filterHighPriority(List<Item> items) {
        return items.stream()
                .filter(item -> item.isHighPriority())
                .collect(Collectors.toList());
    }

    static List<Item> filterByKeyword(List<Item> items, String keyword) {
        return items.stream()
                .filter(item -> item.name.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    static boolean hasUrgentItem(List<Item> items) {
        return items.stream()
                .anyMatch(Item::isHighPriority);
    }

    static long countUrgentItems(List<Item> items) {
        return items.stream()
                .filter(Item::isHighPriority)
                .count();
    }

    static List<String> getDistinctZones(List<Item> items) {
        return items.stream()
                .map(item -> item.zone)
                .distinct()
                .collect(Collectors.toList());
    }
}
