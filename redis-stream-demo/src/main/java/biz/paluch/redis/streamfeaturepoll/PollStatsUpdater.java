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
package biz.paluch.redis.streamfeaturepoll;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamReceiver;

/**
 * Updater component that listens to the Stream {@code feature_poll} and increments a counter in Sorted Set
 * {@code poll_stats}.
 *
 * @author Mark Paluch
 */
@RequiredArgsConstructor
@Slf4j
public class PollStatsUpdater {

	private final StreamReceiver<String, ObjectRecord<String, VoteMessage>> streamReceiver;
	private final RedisReactiveCommands<String, String> commands;

	private Disposable subscription;

	@PostConstruct
	private void postConstruct() {

		Flux<ObjectRecord<String, VoteMessage>> feature_poll_stream = streamReceiver
				.receive(StreamOffset.fromStart("feature_poll"));

		subscription = feature_poll_stream //
				.flatMap(it -> {

					log.info("Processing message: " + it);

					VoteMessage vote = it.getValue();

					return commands.zaddincr("poll_stats", 1, vote.getFeature());
				}) //
				.subscribe(x -> {}, Throwable::printStackTrace);
	}

	@PreDestroy
	private void preDestroy() {
		if (subscription != null) {
			subscription.dispose();
			subscription = null;
		}
	}
}
