package de.frozenbytes.kickermost.io.src.rss;

import de.frozenbytes.kickermost.BasicTest;
import de.frozenbytes.kickermost.conf.PropertiesHolder;
import de.frozenbytes.kickermost.conf.PropertiesLoader;
import de.frozenbytes.kickermost.dto.property.RssLink;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RssParserTest extends BasicTest {

    private static PropertiesHolder propertiesHolder;
    private RssParser parser;

    @BeforeClass
    public static void beforeClass() throws Exception {
        propertiesHolder = PropertiesLoader.createPropertiesHolder(CONFIG_FILEPATH);
    }

    @Before
    public void before() throws IOException {
        parser = new RssParser(propertiesHolder);
    }

    @Test
    public void test() throws Exception {
        final List<RssLink> linkList = parser.parse();
        assertThat(linkList).isNotNull().isNotEmpty();
        assertThat(linkList.stream().allMatch(link -> link.getValue().matches("^http://.*$"))).isTrue();
        linkList.forEach(System.out::println);
    }

}
