interface AlertRule<T> {    //generic interface type T
    boolean check(T item);// generic interface filtering, checks if item true or false
}

class SmallItemAlertRule implements AlertRule<Item> {  
    
    public boolean check(Item item) {//interface is implemented here and type T is Item

        return item instanceof SmallItem;   //check if this object is of class SmallItem
    }
}
