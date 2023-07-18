package br.com.banco.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Pageable;
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
	
	
	public TransferenciaController(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}
	
	@GetMapping("/{id}")
	public PageDTO<Transferencia> buscarTransferenciasPorIdConta(
			Pageable pageable,
			@PathVariable @NotNull @Positive Long id){
		return transferenciaService.buscarTransferenciasPorIdConta(pageable, id);
	}
	
	@GetMapping
	public PageDTO<Transferencia> filtroParaAsTransferencias(
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "") String nome, 
			@RequestParam(required = false, defaultValue = "") String dataInicio,
			@RequestParam(required = false, defaultValue = "") String dataFim){
		// Por nome do operador + período
		if(!nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			return transferenciaService.buscarTransferenciasPorNomeOperadorTransacaoEPorPeriodo(pageable, dataInicio, dataFim, nome);
		}
		// Por nome do operador
		if(!nome.isBlank() && dataInicio.isBlank() & dataFim.isBlank()) {
			
			return transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(pageable, nome);
		}
		// Por período
		if(nome.isBlank() && !dataInicio.isBlank() & !dataFim.isBlank()) {
			return transferenciaService.buscarTransferenciasPorPeriodo(pageable, dataInicio, dataFim);
		}
		// Todos
		return transferenciaService.listarTodasAsTransferencias(pageable);
		
	}
}
