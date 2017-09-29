module elasticsearch.client.v2 {
    requires elasticsearch.client.core;
    requires elasticsearch.shaded;
    provides elasticsearch.client.spi.SearchClient with elasticsearch.client.v2.SearchClientImpl;
}