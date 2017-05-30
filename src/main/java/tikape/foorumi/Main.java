package tikape.foorumi;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.foorumi.database.Database;
import tikape.foorumi.database.AihealueDao;
import tikape.foorumi.database.KeskustelunavausDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:data/testidata.db");

        AihealueDao aihealueDao = new AihealueDao(database);
        KeskustelunavausDao keskustelunavausDao = new KeskustelunavausDao(database);

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

    }
}
