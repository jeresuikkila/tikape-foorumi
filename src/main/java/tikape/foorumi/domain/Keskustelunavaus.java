package tikape.foorumi.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jere
 */
public class Keskustelunavaus {

    private Integer id;
    private Integer aihealue;
    private String aihe;
    private String timestamp;
    private String viesti;
    private String nimimerkki;
    private int viesteja;
    private String viimeisinViesti;
    
    public Keskustelunavaus(Integer id, Integer aihealue, String aihe, String timestamp, String viesti, String nimimerkki) {
        this.id = id;
        this.aihealue = aihealue;
        this.aihe = aihe;
        this.timestamp = timestamp;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }

    public Keskustelunavaus(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.aihealue = rs.getInt("aihealue");
        this.aihe = rs.getString("aihe");
        this.timestamp = rs.getString("timestamp");
        this.viesti = rs.getString("viesti");
        this.nimimerkki = rs.getString("nimimerkki");
    }

    public String getViimeisinViesti() {
        return this.viimeisinViesti;
    }
    
    public void setViimeisinViesti(String v) {
        this.viimeisinViesti = v;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAihe() {
        return aihe;
    }

    public String getViesti() {
        return viesti;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public Integer getAihealue() {
        return aihealue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getViesteja() {
        return viesteja;
    }

    public void setViesteja(int viesteja) {
        this.viesteja = viesteja;
    }
    

}
