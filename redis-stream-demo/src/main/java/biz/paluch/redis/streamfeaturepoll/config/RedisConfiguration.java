/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package biz.paluch.redis.streamfeaturepoll.config;

import biz.paluch.redis.streamfeaturepoll.PollStatsUpdater;
import biz.paluch.redis.streamfeaturepoll.VoteMessage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.data.redis.stream.StreamReceiver.StreamReceiverOptions;

/**
 * @author Mark Paluch
 */
@Configuration
@EnableConfigurationProperties(PollProperties.class)
class RedisConfiguration {

	@Bean
	public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

		RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
				.<String, String> newSerializationContext(RedisSerializer.string()).hashValue(SerializationPair.raw())
				.hashKey(SerializationPair.raw()).build();

		return new ReactiveRedisTemplate<>(factory, serializationContext);
	}

	@Bean
	@ConditionalOnProperty("stream.poll-enabled")
	public StreamReceiver<String, ObjectRecord<String, VoteMessage>> streamReceiver(
			ReactiveRedisConnectionFactory factory) {
		return StreamReceiver.create(factory, StreamReceiverOptions.builder().bodyType(VoteMessage.class).build());
	}

	@Bean
	@ConditionalOnProperty("stream.poll-enabled")
	public PollStatsUpdater updater(StreamReceiver<String, ObjectRecord<String, VoteMessage>> streamReceiver,
			RedisReactiveCommands<String, String> commands) {
		return new PollStatsUpdater(streamReceiver, commands);
	}

	@Bean(destroyMethod = "close")
	public StatefulRedisConnection<String, String> connection(ReactiveRedisConnectionFactory factory) {

		DirectFieldAccessor accessor = new DirectFieldAccessor(factory);

		RedisClient client = (RedisClient) accessor.getPropertyValue("client");

		return client.connect();
	}

	@Bean
	public RedisReactiveCommands<String, String> commands(StatefulRedisConnection<String, String> connection) {
		return connection.reactive();
	}
}
