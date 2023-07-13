package br.com.banco.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Transferencia>> buscarTransferenciasPorIdConta(@PathVariable @NotNull @Positive Long id){
		return ResponseEntity.ok(transferenciaService.buscarTransferenciasPorIdConta(id));
	}
	
	@GetMapping
	public ResponseEntity<List<Transferencia>> filtroParaAsTransferencias(
			@RequestParam(required = false, defaultValue = "") @Pattern(regexp= "^[a-zA-Z ]{1,50}$") String nome, 
			@RequestParam(required = false, defaultValue = "") @Pattern(regexp= "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$") String dataInicio,
			@RequestParam(required = false, defaultValue = "") @Pattern(regexp= "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$") String dataFim){
		if(!nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = LocalDateTime.parse(dataInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dataF = LocalDateTime.parse(dataFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return ResponseEntity.ok(transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nome)
					.stream()
					.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
					.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
					.collect(Collectors.toList()));
		}
		if(!nome.isBlank() && dataInicio.isBlank() & dataFim.isBlank()) {
			return ResponseEntity.ok(transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(nome));
		}
		if(nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			LocalDateTime dataI = LocalDateTime.parse(dataInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dataF = LocalDateTime.parse(dataFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return ResponseEntity.ok( transferenciaService.listarTodasAsTransferencias()
					.stream()
					.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
					.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
					.collect(Collectors.toList()));
		}
		return ResponseEntity.ok(transferenciaService.listarTodasAsTransferencias());

	}
}
