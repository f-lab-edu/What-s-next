package com.bjcareer.search.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.controller.dto.StockAdditionRequestDTO;
import com.bjcareer.search.controller.dto.StockAdditionResponseDTO;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.service.StockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
@Slf4j
public class StockController {
	private final StockService stockService;

	//주식 추가
	@PostMapping
	public ResponseEntity addStockOfThema(@Valid @RequestBody StockAdditionRequestDTO requestDTO) {
		log.debug("request: {}", requestDTO);

		Thema thema = stockService.addStockThema(requestDTO.getCode(), requestDTO.getStockName(),
			requestDTO.getThema());

		StockAdditionResponseDTO responseDTO = new StockAdditionResponseDTO(thema.getId(),
			thema.getStock().getName(), thema.getThemaInfo().getName(), thema.getStock().getCode());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new Response<>(HttpStatus.CREATED, "CREATED", responseDTO));
	}
}
