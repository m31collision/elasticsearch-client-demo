package elasticsearch.client.v1;

import elasticsearch.client.spi.SearchClient;
import org.elasticsearch.Version;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.*;
import org.elasticsearch.common.transport.*;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.*;
import java.util.Map;

public class SearchClientImpl implements SearchClient {
	private final Settings settings = ImmutableSettings.builder()
			.put("cluster.name", "es1")
			.put("node.name", "es1")
			.build();

	private final Client searchClient = new TransportClient(settings)
			.addTransportAddress(getAddress());

	private InetSocketTransportAddress getAddress() {
		return new InetSocketTransportAddress("127.0.0.1", 9301);
	}

	@Override
	public String getVersion() {
		return Version.CURRENT.number();
	}

	@Override
	public Map search(String term) {
		SearchResponse response = searchClient.prepareSearch("*")
				.setQuery(QueryBuilders.termQuery("field", term))
				.execute()
				.actionGet();
		if (response.getHits().getTotalHits() > 0) {
			return response.getHits().getAt(0).getSource();
		} else {
			return null;
		}
	}

	@Override
	public void close() {
		searchClient.close();
	}
}
