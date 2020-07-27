import org.json.JSONObject;
import spark.Spark;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        DataBase dataBase = new DataBase("scannerappdatabase");

        //http://localhost:4567/home
        Spark.get("/home", (request, response) -> getTextFromFile("home.html"));
        Spark.post("/login", (request, response) -> {
            String email = request.queryParams("email");
            String password = request.queryParams("password");
            System.out.println("jusu email: " + email + "Jusu slaptazodis: " + password);
            User user = dataBase.login(email, password);
            if (user != null) {
                JSONObject json = new JSONObject();
                json.put("email", user.getEmail());
                json.put("password", user.getPassword());
                return json.toString();
            } else {
                response.status(400);
                return "Can't login try again";
            }
        });
        Spark.get("/registration", (request, response) -> getTextFromFile("registration.html"));
        Spark.post("/registration", (request, response) -> {
            String email = request.queryParams("email");
            String password = request.queryParams("password");
            int user = dataBase.registration(email, password);

                return "Registration success";

        });
        Spark.get("/desktop", (request, response) -> getTextFromFile("desktop.html"));
        Spark.post("/desktop", (request, response) -> {
            int id = Integer.parseInt(request.queryParams("id"));
            String cardbarcode = request.queryParams("cardbarcode");
            System.out.println("nuskanuotas barcode yra: " + cardbarcode);
            Wallet wallet = dataBase.addCard(id,cardbarcode);
            if (wallet != null) {
                JSONObject json = new JSONObject();
                json.put("id", wallet.getId());
                json.put("cardbarcode", wallet.getCardbarcode());
                return json.toString();
            } else {
                response.status(400);
                return "Can't add barcode";
            }
        });
    }

        private static String getTextFromFile (String path){
            try {
                URI fullPath = Main.class.getClassLoader().getResource(path).toURI();
                return new String(Files.readAllBytes(Paths.get(fullPath)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Loading error";
        }

    }


