package example.executablejar;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.executablejar.model.ResponseModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Properties;
import java.util.Random;

public class Main {

    public static void main(String... args) throws Exception {
        Properties props = new Properties();
        props.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));

        System.out.println("Loaded property from property file: " + props.get("test.property"));

        Random rand = new Random(System.currentTimeMillis());
        OkHttpClient httpClient = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .get()
                .url("http://httpbin.org/get?test=" + rand.nextInt((100 - 1) + 1) + 1)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseModel model = mapper.readerFor(ResponseModel.class).readValue(response.body().bytes());
                System.out.println("Response from HTTP endpoint: " + model.getArgs().get("test"));
            } else {
                System.out.println("Error contacting mock http service: " + response.code());
            }
        }
    }
}
