package tikape.foorumi;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.foorumi.database.Database;
import tikape.foorumi.database.AihealueDao;
import tikape.foorumi.database.KeskustelunavausDao;
import tikape.foorumi.database.ViestiDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:data/testidata.db");

        AihealueDao aihealueDao = new AihealueDao(database);
        KeskustelunavausDao keskustelunavausDao = new KeskustelunavausDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        get("/", (req, res) -> {
            String nimi = req.queryParams("aihealue");
            if (nimi != null && !nimi.trim().isEmpty()) {
                aihealueDao.create(nimi);
            }

            HashMap map = new HashMap<>();
            map.put("aihealueet", aihealueDao.findAll());

            return new ModelAndView(map, "aihealueet");
        }, new ThymeleafTemplateEngine());

        get("/aihealue/:id", (req, res) -> {
            int aihealue = Integer.parseInt(req.params("id"));
            
            String aihe = req.queryParams("aihe");
            String viesti = req.queryParams("viesti");
            String nimimerkki = req.queryParams("nimimerkki");
            if (aihe != null && viesti != null && nimimerkki != null &&
                !aihe.trim().isEmpty() && !viesti.trim().isEmpty() && !nimimerkki.trim().isEmpty()) {
                keskustelunavausDao.create(aihealue, aihe, viesti, nimimerkki);
            }
            
            
            
            HashMap map = new HashMap<>();
            map.put("aihealue", aihealueDao.findOne(aihealue));
            map.put("keskustelunavaukset", keskustelunavausDao.findAllInAihealue(aihealue));

            return new ModelAndView(map, "aihealue");
        }, new ThymeleafTemplateEngine());

        get("/aihealue/:aihe/keskustelu/:id", (req, res) -> {
            int aihe = Integer.parseInt(req.params("aihe"));
            int id = Integer.parseInt(req.params("id"));
            HashMap map = new HashMap<>();
            map.put("aihealue", aihealueDao.findOne(aihe));
            map.put("keskustelunavaus", keskustelunavausDao.findOne(id));
            map.put("viestit", viestiDao.findAllInKeskustelunavaus(id));

            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());

    }
}
