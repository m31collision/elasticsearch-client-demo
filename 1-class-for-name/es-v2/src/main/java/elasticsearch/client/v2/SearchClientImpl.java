package elasticsearch.client.v2;

import elasticsearch.client.spi.SearchClient;
import org.elasticsearch.Version;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.*;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.*;
import java.net.*;
import java.util.Map;

public class SearchClientImpl implements SearchClient {
	private final Settings settings = Settings.builder()
			.put("cluster.name", "es2")
			.put("node.name", "es2")
			.build();

	private final Client searchClient = TransportClient.builder()
			.settings(settings)
			.build()
			.addTransportAddress(getAddress());

	private static TransportAddress getAddress() {
		try {
			return new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
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
