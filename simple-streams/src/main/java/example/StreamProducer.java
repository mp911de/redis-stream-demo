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
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Write the current time to the Stream at key {@code my_stream}.
 * 
 * @author Mark Paluch
 */
public class StreamProducer {

	private static final Logger LOGGER = Logger.getLogger("example.StreamProducer");

	public static void main(String[] args) throws Exception {

		RedisClient client = RedisClient.create("redis://localhost");
		StatefulRedisConnection<String, String> connection = client.connect();
		RedisStreamCommands<String, String> commands = connection.sync();

		while (true) {

			Map<String, String> body = Collections.singletonMap("time", LocalDateTime.now().toString());
			LOGGER.info(String.format("Adding message with body: %s", body));

			commands.xadd("my_stream", body);

			Thread.sleep(1000);
		}
	}
}
