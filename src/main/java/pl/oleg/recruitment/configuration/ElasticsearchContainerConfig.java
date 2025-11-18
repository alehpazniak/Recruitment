package pl.oleg.recruitment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class ElasticsearchContainerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ElasticsearchContainer elasticsearchContainer() {
        ElasticsearchContainer container =
            new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.8.0"))
                .withEnv("discovery.type", "single-node")
                .withEnv("xpack.security.enabled", "false")
                .withExposedPorts(9200);
        return container;
    }

    @Bean
    public String elasticsearchUrl(ElasticsearchContainer container) {
        return container.getHttpHostAddress();
    }
}
