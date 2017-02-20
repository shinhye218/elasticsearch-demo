package ruxing.demo.core;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ruxing on 20/02/2017.
 */
@Slf4j
@Service
public class ClientManager {

    private TransportClient client;

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;

    public Client getClient() {
        if (client == null) {
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch").build();
            try {
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            } catch (UnknownHostException e) {
                log.error("TransportClient创建失败" + e);
            }
        }
        return client;
    }

}
