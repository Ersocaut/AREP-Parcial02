import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.port;

public class Proxy {

    public static int ind = 0;

    public static void main(String[] args) {
        port(getPort());
        get("/hello", (req, res) -> "Hello world from spark.");

        get("/ln", (req, res) -> {
            res.type("application/json");
            return getServer("/ln", Double.parseDouble(req.queryParams("value")));
        });

        get("/asin", (req, res) -> {
            res.type("application/json");
            return getServer("/asin", Double.parseDouble(req.queryParams("value")));
        });
    }

    static int getPort(){
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }return 4567;
    }

    static String server1(){
        if (System.getenv("SERVER1") != null) {
            return System.getenv("SERVER1").toString();
        }return "http://localhost:4567";
    }

    static String server2(){
        if (System.getenv("SERVER2") != null) {
            return System.getenv("SERVER2").toString();
        }return "http://localhost:1234";
    }

    static String getServer(String op, double value) throws IOException {
        ArrayList<String> servers = new ArrayList<>();
        servers.add(server1());
        servers.add(server2());
        //servers.add("http://localhost:4567");
        //servers.add("http://localhost:1234");
        ind++;
        if (ind >= servers.size() - 1) {
            ind = 0;
        }
        String ret = "";
        URL url = new URL(servers.get(ind) + op + "?value=" + value);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            ret += response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println(ret);
        return ret;
    }
}
