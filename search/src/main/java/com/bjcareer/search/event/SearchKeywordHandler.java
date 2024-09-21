package com.bjcareer.search.event;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.service.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchKeywordHandler {
	Queue<SearchedKeyword> eventQueue = new LinkedList<>();
	private final RankingService rankingService;

	@EventListener
	public void handle(SearchedKeyword event) {
		log.debug("SearchKeywordHandler: {}", event.getKeyword());
		eventQueue.add(event);
	}

	@Scheduled(fixedDelay = 5000)  // 5초마다 실행
	public void processQueue() {
		while (!eventQueue.isEmpty()) {
			SearchedKeyword event = eventQueue.poll();
			log.debug("Processing keyword: {}", event.getKeyword());
			rankingService.updateKeyword(event.getKeyword());
		}
	}
}
