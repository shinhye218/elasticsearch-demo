package test.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ruxing.demo.core.ClientManager;
import ruxing.demo.entity.po.User;
import ruxing.demo.service.SearchService;

import java.util.List;

/**
 * Created by ruxing on 18/02/2017.
 */
@ContextConfiguration(locations = { "classpath*:/test/spring.xml"})
public class SearchTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ClientManager clientManager;

    @Autowired
    SearchService searchService;

    @Test
    public void searchDemo() {
        List<User> user = searchService.searchDemo();
        System.out.println(user.size());
    }

}
