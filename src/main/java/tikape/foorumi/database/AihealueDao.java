package tikape.foorumi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.foorumi.domain.Aihealue;

public class AihealueDao implements Dao<Aihealue, Integer> {

    private final Database database;

    public AihealueDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihealue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Aihealue a = new Aihealue(id, nimi, countViestit(id) + countKeskustelunavaukset(id));

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Aihealue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue");

        ResultSet rs = stmt.executeQuery();
        List<Aihealue> aihealueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            aihealueet.add(new Aihealue(id, nimi, countViestit(id) + countKeskustelunavaukset(id)));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aihealueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // TODO: aihealueen poistaminen
    }

    public int countKeskustelunavaukset(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Keskustelunavaus WHERE aihealue = ?");

        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        int count = rs.getInt(1);
        return count;
    }

    public int countViestit(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Viesti WHERE Keskustelunavaus IN (SELECT id FROM Keskustelunavaus WHERE aihealue = ?)");

        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        int count = rs.getInt(1);
        return count;
    }

}
