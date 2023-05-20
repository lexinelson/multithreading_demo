import java.util.List;

public class Main {

    public static void main(String... args) {
        measureOperationTime("Linear operation", Main::getApiDataLinear);
    }

    public static void measureOperationTime(String label, Runnable runnable) {
        System.out.println("Running " + label);
        long startTime = System.currentTimeMillis();
        runnable.run();
        System.out.println(label + ": " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

    public static void getApiDataLinear() {
        ApiGuru guru = new ApiGuru("api-services_linear.txt");
        List<String> providers = guru.getApiProviders();
        for (String provider : providers) {
            guru.storeServices(provider);
        }
    }
}
