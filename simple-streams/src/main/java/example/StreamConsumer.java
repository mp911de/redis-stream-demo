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
package example;

import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs.Builder;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 * Consume the stream {@code my_stream}.
 *
 * @author Mark Paluch
 */
@SuppressWarnings({ "unchecked", "lettuce", "xconsume" })
public class StreamConsumer {

	private static final Logger LOGGER = LogManager.getLogger(StreamConsumer.class);

	public static void main(String[] args) throws Exception {

		RedisClient client = RedisClient.create("redis://localhost");
		StatefulRedisConnection<String, String> connection = client.connect();
		RedisStreamCommands<String, String> commands = connection.sync();

		String lastSeenMessage = "0-0";
		while (true) {

			List<StreamMessage<String, String>> messages = commands.xread(Builder.block(Duration.ofSeconds(1)),
					StreamOffset.from("my_stream", lastSeenMessage));

			for (StreamMessage<String, String> message : messages) {
				lastSeenMessage = message.getId();
				LOGGER.info(String.format("Received %s", message));
			}
		}

	}
}
