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
}
