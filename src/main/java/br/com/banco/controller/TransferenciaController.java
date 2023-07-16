package br.com.banco.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.banco.dto.PageDTO;
import br.com.banco.model.Transferencia;
import br.com.banco.service.TransferenciaService;

@Validated
@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {
	
	private final TransferenciaService transferenciaService;
	private final String regexData = "^\\d{2}/\\d{2}/\\d{4}$";
	private final String regexNome = "^[a-zA-Z ]{1,50}$";
	
	public TransferenciaController(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PageDTO<Transferencia>> buscarTransferenciasPorIdConta(
			Pageable pageable,
			@PathVariable @NotNull @Positive Long id){
		return new ResponseEntity<>(transferenciaService.buscarTransferenciasPorIdConta(pageable, id), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageDTO<Transferencia>> filtroParaAsTransferencias(
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "") String nome, 
			@RequestParam(required = false, defaultValue = "") String dataInicio,
			@RequestParam(required = false, defaultValue = "") String dataFim){
		if(!nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = transferenciaService.validaData(dataInicio, regexData);
			LocalDateTime dataF = transferenciaService.validaData(dataFim, regexData);
			String nomeValid = transferenciaService.validaNome(nome, regexNome);
			PageDTO<Transferencia> pages = transferenciaService
					.fazPaginas(pageable.getPageNumber(),
			transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nomeValid)
			.stream()
			.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
			.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
			.collect(Collectors.toList()));
			return ResponseEntity.ok(pages);
		}
		if(!nome.isBlank() && dataInicio.isBlank() & dataFim.isBlank()) {
			String nomeValid = transferenciaService.validaNome(nome, regexNome);
			PageDTO<Transferencia> pages = transferenciaService
					.fazPaginas(pageable.getPageNumber(), transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nomeValid));
			return ResponseEntity.ok(pages);
		}
		if(nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = transferenciaService.validaData(dataInicio, regexData);
			LocalDateTime dataF = transferenciaService.validaData(dataFim, regexData);
			PageDTO<Transferencia> pages = transferenciaService
					.fazPaginas(pageable.getPageNumber(),
							transferenciaService.listarTodasAsTransferencias()
							.stream()
							.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
							.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
							.collect(Collectors.toList()));
			return ResponseEntity.ok(pages);
		}
		PageDTO<Transferencia> pages = transferenciaService
				.fazPaginas(pageable.getPageNumber(),transferenciaService.listarTodasAsTransferencias());
		return ResponseEntity.ok(pages);

	}
}
