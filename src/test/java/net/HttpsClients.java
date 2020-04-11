package net;

import lombok.Value;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Value
public class HttpsClients {
    static Executor cachedExecutor = Executors.newCachedThreadPool();

    public static HttpClient getNewClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .executor(cachedExecutor)
                .build();
    }
}
