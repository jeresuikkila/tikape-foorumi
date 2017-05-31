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
            HashMap map = new HashMap<>();
            map.put("aihealueet", aihealueDao.findAll());

            return new ModelAndView(map, "aihealueet");
        }, new ThymeleafTemplateEngine());

        get("/aihealue/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            HashMap map = new HashMap<>();
            map.put("aihealue", aihealueDao.findOne(id));
            map.put("keskustelunavaukset", keskustelunavausDao.findAllInAihealue(id));

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
