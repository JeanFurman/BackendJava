package br.com.banco.controller;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.banco.model.Transferencia;
import br.com.banco.service.TransferenciaService;

@Validated
@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {
	
	private final TransferenciaService transferenciaService;
	
	public TransferenciaController(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}
	
	@GetMapping("/{id}")
	public List<Transferencia> buscarTransferenciasPorIdConta(@PathVariable @NotNull @Positive Long id){
		return transferenciaService.buscarTransferenciasPorIdConta(id);
	}
	
	@GetMapping
	public List<Transferencia> filtroParaAsTransferencias(@RequestParam(required = false, defaultValue = "") @Valid String nome, 
			@RequestParam(required = false, defaultValue = "") String dataInicio,
			@RequestParam(required = false, defaultValue = "") String dataFim){
		if(!nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = LocalDateTime.parse(dataInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dataF = LocalDateTime.parse(dataFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nome)
					.stream()
					.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
					.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
					.collect(Collectors.toList());
		}
		if(!nome.isBlank() && dataInicio.isBlank() & dataFim.isBlank()) {
			return transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nome);
		}
		if(nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = LocalDateTime.parse(dataInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dataF = LocalDateTime.parse(dataFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return transferenciaService.listarTodasAsTransferencias()
					.stream()
					.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
					.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
					.collect(Collectors.toList());
		}
		if(nome.isBlank() && dataInicio.isBlank() & dataFim.isBlank()) {
			return transferenciaService.listarTodasAsTransferencias();
		}
		return null;
		
		
	}
}
