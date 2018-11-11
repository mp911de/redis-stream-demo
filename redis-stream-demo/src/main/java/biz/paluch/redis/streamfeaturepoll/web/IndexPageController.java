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
package biz.paluch.redis.streamfeaturepoll.web;

import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Mark Paluch
 */
@RestController
public class IndexPageController {

	@GetMapping(value = "/")
	Mono<Void> index(ServerWebExchange exchange) {

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.SEE_OTHER);
		response.getHeaders().add(HttpHeaders.LOCATION, "/index.html");
		return response.setComplete();
	}
}
