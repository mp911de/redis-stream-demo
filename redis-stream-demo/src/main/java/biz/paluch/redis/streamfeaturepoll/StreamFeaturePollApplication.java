package biz.paluch.redis.streamfeaturepoll;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Mark Paluch
 */
@SpringBootApplication
@RequiredArgsConstructor
public class StreamFeaturePollApplication {

	public final RedisTemplate<String, String> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(StreamFeaturePollApplication.class, args);
	}

	@PostConstruct
	private void postConstruct() {
		redisTemplate.delete("poll_stats");
	}
}
