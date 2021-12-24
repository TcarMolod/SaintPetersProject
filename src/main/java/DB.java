import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DB {

    public static Connection connection;
    private static final String database = "test2.db";

    public DB() {
        try {
            // Открываем соединение с базой данных

            connection = DriverManager.getConnection("jdbc:sqlite:" + database);

            // Создаём таблицы
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS location ("
                    + " number VARCHAR(255),"
                    + " snapshot VARCHAR(255),"
                    + " appellation VARCHAR(255),"
                    + " id_ VARCHAR(255),"
                    + " house_id integer"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS house ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " address VARCHAR(255),"
                    + " prefix_code VARCHAR(255),"
                    + " building_type VARCHAR(255),"
                    + " year_construction VARCHAR(255),"
                    + " number_of_floor VARCHAR(255)"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLocation(String number, String snapshot, String app, String id, int house_id) throws SQLException {
        PreparedStatement insertStmt = connection.prepareStatement(
                "INSERT INTO location(number, snapshot, appellation, id_, house_id) VALUES(?, ?, ?, ?, ?)");
        insertStmt.setString(1,number);
        insertStmt.setString(2,snapshot);
        insertStmt.setString(3,app);
        insertStmt.setString(4,id);
        insertStmt.setInt(5,house_id);
        insertStmt.executeUpdate();
    }

    public int addHouse(String address, String prefix_code, String building_type, String year_constr, String number_of_floor) throws SQLException {
        var sql = "INSERT INTO house(address, prefix_code, building_type, year_construction, number_of_floor) VALUES(?, ?, ?, ?, ?)";

        PreparedStatement insertStmt = connection.prepareStatement(
                sql);
        insertStmt.setString(1, address);
        insertStmt.setString(2, prefix_code);
        insertStmt.setString(3, building_type);
        insertStmt.setString(4, year_constr);
        insertStmt.setString(5, number_of_floor);
        insertStmt.executeUpdate();
        var a = insertStmt.getGeneratedKeys();
        return a.getInt(1);
    }

    public List<List<String>> getStat_by_floor() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(100);
        ResultSet rs = statement.executeQuery(
                "select number_of_floor, COUNT(*), row_number() OVER(order by number_of_floor) as rec_cnt " +
                        "from house WHERE number_of_floor <>'' " +
                        "group by number_of_floor " +
                        "order by number_of_floor;");
        var res = new ArrayList<List<String>>();
        while (rs.next()) {

            if (rs.getString(1).endsWith("этажный")) {
                res.add(new ArrayList<String>());
                res.get(res.size() - 1).add(rs.getString(1));
                res.get(res.size() - 1).add(rs.getString(2));
            }
        }
        res.sort(Comparator.comparing(e->Integer.parseInt(e.get(0).substring(0,e.get(0).length()-8))));
        return res;
    }

    public List<List<String>> getLocations_by_prefix_code(String prefix_code) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select l.number, h.address, l.snapshot, l.appellation, h.number_of_floor, h.prefix_code, h.building_type, l.id_, h.year_construction FROM location l INNER JOIN house h on h.id=l.house_id WHERE h.prefix_code=?;");
        statement.setQueryTimeout(100);
        statement.setString(1,prefix_code);
        ResultSet rs = statement.executeQuery();

        var res = new ArrayList<List<String>>();
        while (rs.next()) {
            if (rs.getString(4).endsWith("Зарегистрированный участок")) {
                res.add(new ArrayList<String>());
                res.get(res.size() - 1).add(rs.getString(1));
                res.get(res.size() - 1).add(rs.getString(2));
                res.get(res.size() - 1).add(rs.getString(3));
                res.get(res.size() - 1).add(rs.getString(4));
                res.get(res.size() - 1).add(rs.getString(5));
                res.get(res.size() - 1).add(rs.getString(6));
                res.get(res.size() - 1).add(rs.getString(7));
                res.get(res.size() - 1).add(rs.getString(8));
                res.get(res.size() - 1).add(rs.getString(9));
            }
        }

        return res;
    }

    public List<List<String>> getUniversities_with_year_const_and_avg_prefix_code() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "select l.number, h.address, l.appellation, h.number_of_floor, h.year_construction, h.prefix_code FROM location l INNER JOIN house h on h.id=l.house_id WHERE h.year_construction<>'' AND (l.appellation LIKE '%Университет' OR l.appellation LIKE '%университет')");
        statement.setQueryTimeout(100);
        ResultSet rs = statement.executeQuery();

        var res = new ArrayList<List<String>>();
        double avg_prefix_code=0;
        while (rs.next()) {
            if (rs.getString(4).endsWith("этажный")
                    && Integer.parseInt(rs.getString(4).substring(0,rs.getString(4).length()-8))>5) {
                res.add(new ArrayList<String>());
                res.get(res.size() - 1).add(rs.getString(1));
                res.get(res.size() - 1).add(rs.getString(2));
                res.get(res.size() - 1).add(rs.getString(3));
                res.get(res.size() - 1).add(rs.getString(4));
                res.get(res.size() - 1).add(rs.getString(5));
                res.get(res.size() - 1).add(rs.getString(6));
                avg_prefix_code += Integer.parseInt(rs.getString(6));
            }
        }
        avg_prefix_code /= res.size();
        res.add(new ArrayList<>());
        res.get(res.size()-1).add("AVG PREFIX_CODE");
        res.get(res.size()-1).add(String.valueOf(avg_prefix_code));
        return res;
    }
}
