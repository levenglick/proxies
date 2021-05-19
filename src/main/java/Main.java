import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {


    public static void main(String[] args) throws IOException {
        String json = "{\n" +
                "    \"ProxyID\":\"campusmuser\"," +
                "    \"ProxyPassword\":\"jaik7In5ego2sha3\"," +
                "    \"UserID\":\"person.test\"" +
                "}";

        String url = "https://col-webapi-ext.ccac.edu:8174/colleagueApi/session/proxy-login";
//        System.out.println(post(url, json));
        System.out.println(postOk(url, json));
//        System.out.println(restPost(url, json));
    }

    private static String restPost(String url, String json) throws IOException {
        RestTemplate template = new RestTemplate();
        ClientHttpRequest request = new SimpleClientHttpRequestFactory().
                createRequest(URI.create(url), HttpMethod.POST);
        final var headers = request.getHeaders();
        String[] properties = "Content-Type,application/json; charset=utf-8,Host,col-webapi-ext.ccac.edu:8174,Connection,Keep-Alive,Accept-Encoding,gzip,User-Agent,okhttp/4.9.1".split(",");
        for (int i = 0; i < properties.length; i++) {
            headers.add(properties[i++], properties[i]);
        }
        final var body = request.getBody();
        body.write(json.getBytes(StandardCharsets.UTF_8));
        body.flush();

        final ClientHttpResponse execute = request.execute();
        final ResponseEntity<String> stringResponseEntity = template.postForEntity(url, json, String.class);
        return stringResponseEntity.getBody();
    }

    private static String postOk(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static HttpLoggingInterceptor interceptor;

    static OkHttpClient client;

    static {
        interceptor = new HttpLoggingInterceptor(System.out::println);
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }


    static String post(String url, String json) throws IOException {
        URL target = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) target.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Encoding", "application/json; charset=utf-8");

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(json);
        writer.flush();

        final var responseCode = conn.getResponseCode();
        System.out.println(responseCode);

        final var errorStream = conn.getErrorStream();
        if (errorStream != null) {
            return IOUtils.toString(errorStream, StandardCharsets.UTF_8);
        }

        return IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
    }

}
