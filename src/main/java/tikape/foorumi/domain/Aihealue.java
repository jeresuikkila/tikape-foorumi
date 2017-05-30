package tikape.foorumi.domain;

/**
 *
 * @author jere
 */
public class Aihealue {
    private Integer id;
    private String nimi;
    private int viesteja;

    public Aihealue(Integer id, String nimi, int viesteja) {
        this.id = id;
        this.nimi = nimi;
        this.viesteja = viesteja;
    }

    public Aihealue(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }

    public int getViesteja() {
        return viesteja;
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
}
