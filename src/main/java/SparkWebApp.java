import static spark.Spark.*;

import services.MathService;

public class SparkWebApp {

    public static void main(String[] args) {
        port(getPort());
        get("/hello", (req, res) -> "Hello world from spark.");

        get("/ln", (req, res) -> {
            res.type("application/json");
            double sol = MathService.getLn(Double.parseDouble(req.queryParams("value")));
            return "{\"operation\":\"ln\"," +
                    "\"input\": " + Double.parseDouble(req.queryParams("value")) + "," +
                    "\"output\":" + sol + "}";
        });

        get("/asin", (req, res) -> {
            res.type("application/json");
            double sol = MathService.getAsin(Double.parseDouble(req.queryParams("value")));
            return "{\"operation\":\"asin\"," +
                    "\"input\": " + Double.parseDouble(req.queryParams("value")) + "," +
                    "\"output\":" + sol + "}";
        });
    }

    static int getPort(){
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }return 4567;
    }


}
