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

import biz.paluch.redis.streamfeaturepoll.PollVote;
import biz.paluch.redis.streamfeaturepoll.VoteMessage;
import biz.paluch.redis.streamfeaturepoll.VoteService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark Paluch
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PollController {

	private final VoteService voteService;

	@PostMapping("/polls/feature")
	Mono<Void> submitPoll(@RequestBody Mono<PollVote> pollRequest,
			@RequestHeader(HttpHeaders.USER_AGENT) String userAgent) {

		return pollRequest.flatMap(request -> {

			VoteMessage message = new VoteMessage(request.getFeature(), userAgent);

			return voteService.vote(message);
		}).then();
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public Mono<Void> handle(Throwable t) {
		t.printStackTrace();
		return Mono.empty();
	}
}
