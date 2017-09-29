package elasticsearch.client;

import elasticsearch.client.spi.SearchClient;

import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Searcher {
    public static void main(String[] args) throws Exception {
        List<SearchClient> clients = Arrays.asList(
                getClient("1"),
                getClient("2")
        );
        for (SearchClient client : clients) {
            System.out.printf("Client for version: %s%n", client.getVersion());
            Map doc = client.search("test");
            System.out.println("Found doc:");
            System.out.println(doc);
            System.out.println();
        }
        clients.forEach(SearchClient::close);
    }

    private static SearchClient getClient(String desiredVersion) throws Exception {
        String className = String.format("elasticsearch.client.v%s.SearchClientImpl", desiredVersion);
        Path moduleDependencies = Paths.get("modules", "es-v" + desiredVersion);
        URL[] jars = Files.list(moduleDependencies)
                .map(Path::toUri)
                .map(Searcher::toURL)
                .toArray(URL[]::new);
        ClassLoader classLoader = new URLClassLoader(jars);
        return (SearchClient) classLoader.loadClass(className).newInstance();
    }

    private static SearchClient getClientUsingCustomClassLoader(String desiredVersion) throws Exception {
        String className = String.format("elasticsearch.client.v%s.SearchClientImpl", desiredVersion);
        Path moduleDependencies = Paths.get("modules", "es-v" + desiredVersion);
        URL[] jars = Files.list(moduleDependencies)
                .map(Path::toUri)
                .map(Searcher::toURL)
                .toArray(URL[]::new);
        ClassLoader classLoader = new ParentLastClassLoader(jars);
        return (SearchClient) classLoader.loadClass(className).newInstance();
    }

    private static URL toURL(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
