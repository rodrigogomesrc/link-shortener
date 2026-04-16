package space.rodrigorocha.short_url.short_url_creator.config;

import space.rodrigorocha.short_url.short_url_creator.model.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableRedisRepositories(basePackages = "repository")
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(
            @Value("${redis.host:localhost}") String redisHost,
            @Value("${redis.port:6379}") int redisPort) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        return new JedisConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Url> shortUrlRedisTemplate(JedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Url> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        JacksonJsonRedisSerializer<Url> serializer = new JacksonJsonRedisSerializer<>(Url.class);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
