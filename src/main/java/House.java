import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class House {
    public String Address;
    public String Prefix_code;
    public String Building_type;
    public String Year_construction;
    public String Number_of_floor;
    public int id_;

    public static final Map<String, House> houses = new HashMap<String, House>();

    public House(String address, String prefix_code, String building_type, String year_construction, String number_of_floor){
        this.Address=address;
        this.Prefix_code=prefix_code;
        this.Building_type=building_type;
        this.Year_construction=year_construction;
        this.Number_of_floor = number_of_floor;
        this.id_=-1;
        House.houses.put(address,this);
    }
    public static House getOrCreate(String address, String prefix_code, String building_type, String year_construction, String number_of_floor){
        if (House.houses.containsKey(address)){
            return House.houses.get(address);
        }
        return new House(address,prefix_code, building_type, year_construction, number_of_floor);
    }

}
