package tikape.foorumi.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
        
        init();
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Aihealue (id integer PRIMARY KEY, nimi varchar(255) NOT NULL);");

        lista.add("CREATE TABLE Keskustelunavaus (id integer PRIMARY KEY, "
                + "aihealue integer NOT NULL, aihe varchar(255) NOT NULL, "
                + "timestamp timestamp DEFAULT CURRENT_TIMESTAMP, "
                + "viesti text NOT NULL, nimimerkki varchar(255) NOT NULL, "
                + "FOREIGN KEY(aihealue) REFERENCES Aihealue(id));");

        lista.add("CREATE TABLE Viesti(id integer PRIMARY KEY, "
                + "keskustelunavaus integer NOT NULL, "
                + "timestamp timestamp DEFAULT CURRENT_TIMESTAMP, "
                + "viesti text NOT NULL, nimimerkki varchar(255) NOT NULL, "
                + "FOREIGN KEY(keskustelunavaus) REFERENCES Keskustelunavaus(id));");

        return lista;
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Aihealue (id SERIAL PRIMARY KEY, nimi varchar(255) NOT NULL);");

        lista.add("CREATE TABLE Keskustelunavaus (id SERIAL PRIMARY KEY, "
                + "aihealue integer NOT NULL, aihe varchar(255) NOT NULL, "
                + "timestamp timestamp DEFAULT CURRENT_TIMESTAMP, "
                + "viesti text NOT NULL, nimimerkki varchar(255) NOT NULL, "
                + "FOREIGN KEY(aihealue) REFERENCES Aihealue(id));");

        lista.add("CREATE TABLE Viesti(id SERIAL PRIMARY KEY, "
                + "keskustelunavaus integer NOT NULL, "
                + "timestamp timestamp DEFAULT CURRENT_TIMESTAMP, "
                + "viesti text NOT NULL, nimimerkki varchar(255) NOT NULL, "
                + "FOREIGN KEY(keskustelunavaus) REFERENCES Keskustelunavaus(id));");

        return lista;
    }
}
