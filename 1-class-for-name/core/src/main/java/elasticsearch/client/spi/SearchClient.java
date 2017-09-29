package elasticsearch.client.spi;

import java.util.Map;

public interface SearchClient {
    String getVersion();

    Map search(String term);

    void close();
}
