package elasticsearch.client;

import elasticsearch.client.spi.SearchClient;

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
        return (SearchClient) Class.forName(className).newInstance();
    }
}
