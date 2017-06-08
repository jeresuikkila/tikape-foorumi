package tikape.foorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.foorumi.domain.Viesti;

/**
 *
 * @author jere
 */
public class ViestiDao {

    private final Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    public List<Viesti> findAllInKeskustelunavaus(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti v WHERE v.keskustelunavaus = ? ORDER BY timestamp ASC");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            viestit.add(new Viesti(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public List<Viesti> findPaginatedInKeskustelunavaus(Integer key, Integer sivu) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Viesti v WHERE v.keskustelunavaus = ? "
                        + "ORDER BY timestamp ASC "
                        + "LIMIT 10 OFFSET ? ");
        stmt.setObject(1, key);
        stmt.setInt(2, (sivu -1) * 10);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            viestit.add(new Viesti(rs));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public void create(int keskustelunavaus, String viesti, String nimimerkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement
                = connection.prepareStatement("INSERT INTO Viesti (keskustelunavaus, viesti, nimimerkki) VALUES (?, ?, ?)");

        statement.setInt(1, keskustelunavaus);
        statement.setString(2, viesti);
        statement.setString(3, nimimerkki);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

}
