package elasticsearch.client;

import elasticsearch.client.spi.SearchClient;
import org.elasticsearch.Version;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher {
    public static void main(String[] args) throws Exception {
//        System.out.println(ClassLoader.getSystemClassLoader());
//        System.out.println(ClassLoader.getSystemClassLoader().getParent());
        System.out.println(Version.CURRENT);
        System.out.println();
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
    }

    private static SearchClient getClient(String desiredVersion) throws Exception {
        Path moduleDependencies = Paths.get("modules", "es-v" + desiredVersion);
        URL[] jars = Files.list(moduleDependencies)
                .map(Path::toUri)
                .map(Searcher::toURL)
                .toArray(URL[]::new);
        ServiceLoader<SearchClient> serviceLoader = ServiceLoader.load(SearchClient.class, new URLClassLoader(jars, null));
        return serviceLoader.iterator().next();
    }

    private static URL toURL(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
