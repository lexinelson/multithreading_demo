import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String... args) {
        measureOperationTime("MultiThreaded operation", Main::getApiDataMultiThreaded);
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

    public static void getApiDataMultiThreaded() {
        ApiGuru guru = new ApiGuru("api-services_multi.txt");
        List<String> providers = guru.getApiProviders();
        Map<String, List<String>> providersByFirstChar = providers.stream()
                .collect(Collectors.groupingBy(p -> p.substring(0, 1)));
        List<Thread> threads = new ArrayList<>();
        for (List<String> providerGroup : providersByFirstChar.values()) {
            Thread thread = new Thread(() -> {
                for (String provider : providerGroup) {
                    guru.storeServices(provider);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }
}
