package test.search;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ruxing on 18/02/2017.
 */
@ContextConfiguration(locations = { "classpath*:/test/spring.xml"})
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Test
    public void test() {
        try {
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            GetResponse response = client.prepareGet("test", "user", "10").get();
            System.out.println(response.getId());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
