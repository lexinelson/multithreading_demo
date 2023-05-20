import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiGuru {

    private final String baseUrl = "https://api.apis.guru/v2/";

    private final String fileName;

    public ApiGuru(String fileName) {
        this.fileName = fileName;
    }

    private HttpClient createClient() {
        return HttpClient.newHttpClient();
    }

    public List<String> getApiProviders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "providers.json"))
                    .GET()
                    .build();
            return convertDataResponse(createClient().send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (URISyntaxException | IOException | InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }

    private List<String> convertDataResponse(HttpResponse<String> response) {
        return Arrays.stream(response.body().split("\\[")[1].split(","))
                .filter(x -> !x.contains("\"\""))
                .map(x -> x.split("\"")[1])
                .collect(Collectors.toList());
    }

    public void storeServices(String api) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + api + "/services.json"))
                    .GET()
                    .build();
            List<String> response = convertDataResponse(
                    createClient().send(request, HttpResponse.BodyHandlers.ofString())
            );
            writeApiServicesToFile(api, response);
        } catch (URISyntaxException | IOException | InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }

    private synchronized void writeApiServicesToFile(String api, List<String> services) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(api + " -- " + services.toString() + "\n");
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
