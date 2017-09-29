module elasticsearch.client.v1 {
    requires elasticsearch.client.core;
    requires elasticsearch.shaded;
    provides elasticsearch.client.spi.SearchClient with elasticsearch.client.v1.SearchClientImpl;
}