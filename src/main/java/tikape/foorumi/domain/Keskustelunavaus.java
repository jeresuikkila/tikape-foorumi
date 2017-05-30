package tikape.foorumi.domain;

import java.sql.Timestamp;

/**
 *
 * @author jere
 */
public class Keskustelunavaus {
    private Integer id;
    private String nimi;
    private String aihe;
    private Timestamp timestamp;
    private String viesti;
    private String nimimerkki;

    public Keskustelunavaus(Integer id, String nimi, String aihe, Timestamp timestamp, String viesti, String nimimerkki) {
        this.id = id;
        this.nimi = nimi;
        this.aihe = aihe;
        this.timestamp = timestamp;
        this.viesti = viesti;
        this.nimimerkki = nimimerkki;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }    

    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public boolean before(Keskustelunavaus toinen) {
        return timestamp.before(toinen.getTimestamp());
    }
}
