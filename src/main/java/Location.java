public class Location {
    public String Number;
    public String Snapshot;
    public String Appellation;

    public String Id;
    public House house;

    public void setHouse(String address, String prefix_code, String building_type, String year_construction, String number_of_floor) {
        this.house = House.getOrCreate(address,prefix_code,building_type,year_construction, number_of_floor);
    }

    public String ToString() {
        return this.house.Address + " | " + this.Snapshot + " | " + this.Appellation + " | " + this.house.Prefix_code;
    }
}

