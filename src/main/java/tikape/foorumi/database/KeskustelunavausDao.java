package tikape.foorumi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.foorumi.domain.Aihealue;
import tikape.foorumi.domain.Keskustelunavaus;

public class KeskustelunavausDao implements Dao<Keskustelunavaus, Integer> {

    private final Database database;

    public KeskustelunavausDao(Database database) {
        this.database = database;
    }

    @Override
    public Keskustelunavaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelunavaus WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Keskustelunavaus k = new Keskustelunavaus(rs);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Keskustelunavaus> findAll() throws SQLException {
        // TODO: findAll() for Keskustelunavaus
        return null;
    }

    public List<Keskustelunavaus> findAllInAihealue(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        
        // FIXME: There must be a better query for this
        // Viesti.timestamp was added to SELECT because it is required in psql to use ORDER BY
        // See more: https://stackoverflow.com/questions/12693089/pgerror-select-distinct-order-by-expressions-must-appear-in-select-list
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT DISTINCT k.*, Viesti.timestamp FROM Keskustelunavaus k "
                        + "LEFT JOIN Viesti ON k.id = Viesti.keskustelunavaus "
                        + "WHERE k.aihealue = ? ORDER BY Viesti.timestamp DESC");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Keskustelunavaus> keskustelunavaukset = new ArrayList<>();
        while (rs.next()) {
            Keskustelunavaus k = new Keskustelunavaus(rs);

            k.setViesteja(countViestit(k.getId()));
            k.setViimeisinViesti(viimeisinViesti(k.getId()));
            keskustelunavaukset.add(k);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelunavaukset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // TODO: keskustelualueen poistaminen
    }

    public String viimeisinViesti(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT k.timestamp, " +
                "(SELECT MAX(v.timestamp) " +
                "FROM Viesti v WHERE v.keskustelunavaus = ?) AS viimeisin_viesti " +
                "FROM Keskustelunavaus k WHERE k.id = ?;");

        stmt.setInt(1, key);
        stmt.setInt(2, key);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String timestamp = rs.getString(1);
        String viimeisinViesti = rs.getString(2);
        System.out.println("timestamp: " + timestamp);
        System.out.println("Viimeisin viesti: " + viimeisinViesti);

        connection.close();
        
        if (viimeisinViesti == null) {
            return timestamp;
        }
        
        return viimeisinViesti;
    }

    public int countViestit(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Viesti v WHERE v.keskustelunavaus = ?");

        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        connection.close();
        return count;
    }

    public void create(int aihealue, String aihe, String viesti, String nimimerkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement
                = connection.prepareStatement("INSERT INTO Keskustelunavaus (aihealue, aihe, viesti, nimimerkki) VALUES (?, ?, ?, ?)");

        statement.setInt(1, aihealue);
        statement.setString(2, aihe);
        statement.setString(3, viesti);
        statement.setString(4, nimimerkki);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

}
