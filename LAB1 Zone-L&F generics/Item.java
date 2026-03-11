class Item {      //this is  my base class, this will be extended everywhere next
    String name;
    String description;
    String zone;

    Item(String name, String description, String zone) {
        this.name = name;
        this.description = description;
        this.zone = zone;
    }

    public String toString() {     //print format of how the item will be displayed in the portal
        return name + " (" + description + ") - " + zone;
    }
}

class SmallItem extends Item {    //1st child class of base class Item
    SmallItem(String name, String description, String zone) {   //constructor, inherits Item class properties
        super(name, description, zone);
    }
}

class GeneralItem extends Item {    //2nd child class of base class Item
    GeneralItem(String name, String description, String zone) {   //constructor, inherits Item class properties
        super(name, description, zone);
    }
}
