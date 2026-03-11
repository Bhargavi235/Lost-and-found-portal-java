class Item {    //base class Item
    String name;
    String description;
    String zone;
    boolean highPriority;   //small items priority check

    Item(String name, String description, String zone, boolean highPriority) {
        this.name = name;
        this.description = description;
        this.zone = zone;
        this.highPriority = highPriority;
    }

    boolean isHighPriority() {
        return highPriority;
    }

    public String toString() {
        return name + " (" + description + ") - " + zone +
                (highPriority ? " [HIGH PRIORITY]" : "");
    }
}

class SmallItem extends Item {
    SmallItem(String name, String description, String zone) {
        super(name, description, zone, true);
    }
}

class GeneralItem extends Item {
    GeneralItem(String name, String description, String zone) {
        super(name, description, zone, false);
    }
}
