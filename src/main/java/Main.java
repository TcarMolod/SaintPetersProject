import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        String filePath = "path_to_csv";
        List<Location> locations = new ArrayList<Location>();
        var db = new DB();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath, Charset.forName("Cp1251")));) {
            String[] values = null;
            csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                var location = new Location();
                location.Number = values[0];
                location.Snapshot = values[2];
                location.Appellation = values[3];
                location.Id = values[7];
                location.setHouse(values[1], values[5], values[6], values[8], values[4]);
                locations.add(location);
                if (location.house.id_ == -1) {
                    location.house.id_ = db.addHouse(location.house.Address, location.house.Prefix_code, location.house.Building_type, location.house.Year_construction, location.house.Number_of_floor);
                }
                db.addLocation(location.Number, location.Snapshot, location.Appellation, location.Id, location.house.id_);
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

//        first task
        var first_task_data = db.getStat_by_floor();
        for (var e:first_task_data){
            System.out.println(e);

        }
        Chart.make(first_task_data);

//        second task
        var second_task_data = db.getLocations_by_prefix_code("9881");
        for (var e:second_task_data){
            System.out.println(e);
        }
        // third task
        var third_task_data = db.getUniversities_with_year_const_and_avg_prefix_code();
        for (var e:third_task_data){
            System.out.println(e);
        }
    }

}

