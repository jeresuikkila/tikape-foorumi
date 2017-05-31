package tikape.foorumi.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jere
 */
public class Viesti {

    private Integer id;
    private Integer keskustelunavaus;
    private String timestamp;
    private String viesti;
    private String nimimerkki;

    public Viesti(Integer id, Integer keskustelunavaus, String timestamp, String viesti, String nimimerkki) {
        this.id = id;
        this.keskustelunavaus = keskustelunavaus;
        this.timestamp = timestamp;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }
    
    public Viesti(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.keskustelunavaus = rs.getInt("keskustelunavaus");
        this.timestamp = rs.getString("timestamp");
        this.viesti = rs.getString("viesti");
        this.nimimerkki = rs.getString("nimimerkki");
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getId() {
        return id;
    }

    public Integer getKeskustelunavaus() {
        return keskustelunavaus;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public String getViesti() {
        return viesti;
    }
}
