package br.com.banco.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.banco.dto.PageDTO;
import br.com.banco.exception.RegistroNotFoundException;
import br.com.banco.model.Transferencia;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;

@Service
public class TransferenciaService {
	private final ContaRepository contaRepository;
	private final TransferenciaRepository transferenciaRepository;
	private final String regexData = "^\\d{2}/\\d{2}/\\d{4}$";
	private final String regexNome = "^[a-zA-Z ]{1,50}$";

	public TransferenciaService(ContaRepository contaRepository, TransferenciaRepository transferenciaRepository) {
		this.contaRepository = contaRepository;
		this.transferenciaRepository = transferenciaRepository;
	}

	public PageDTO<Transferencia> listarTodasAsTransferencias(Pageable pageable){
		return fazPaginas(pageable.getPageNumber(), transferenciaRepository.findAll());
	}
	
	public PageDTO<Transferencia> buscarTransferenciasPorIdConta(Pageable pageable, Long id) {
		List<Transferencia> l = contaRepository.findById(id)
				.map(c -> transferenciaRepository.findByContaId(c))
				.orElseThrow(() -> new RegistroNotFoundException(String.valueOf(id)));
		return fazPaginas(pageable.getPageNumber(), l);
		
	}
	
	public PageDTO<Transferencia> buscarTransferenciasPorNomeOperadorTransacao(Pageable pageable, String nome){
		return fazPaginas(pageable.getPageNumber(), listarPorNomeDoOperadorTransacao(nome));
	}
	
	public PageDTO<Transferencia> buscarTransferenciasPorPeriodo(Pageable pageable, String dataInicio, String dataFim){	
		return fazPaginas(pageable.getPageNumber(), listarPeriodo(dataInicio, dataFim, transferenciaRepository.findAll()));
	}
	
	public PageDTO<Transferencia> buscarTransferenciasPorNomeOperadorTransacaoEPorPeriodo(Pageable pageable, String dataInicio, String dataFim, String nome){	
		return fazPaginas(pageable.getPageNumber(), listarPeriodo(dataInicio, dataFim, listarPorNomeDoOperadorTransacao(nome)));
	}
	
	private List<Transferencia> listarPorNomeDoOperadorTransacao(String nome) {
		String nomeValid = validaNome(nome, regexNome);
		List<Transferencia> l = transferenciaRepository.findByNomeOperadorTransacao(nomeValid);
		if(l.size() < 1) {
			throw new RegistroNotFoundException(nome);
		}
		return l;
	}
	
	private List<Transferencia> listarPeriodo(String dataInicio, String dataFim, List<Transferencia> lista){
		LocalDateTime dataI = validaData(dataInicio, regexData);
		LocalDateTime dataF = validaData(dataFim, regexData);
		
		return lista.stream()
				.filter(t -> t.getDataTransferencia().equals(dataI) || t.getDataTransferencia().isAfter(dataI))
				.filter(t -> t.getDataTransferencia().equals(dataF) || t.getDataTransferencia().isBefore(dataF))
				.collect(Collectors.toList());
	}
	
	private PageDTO<Transferencia> fazPaginas(int pageNumber, List<Transferencia> lista){
		PagedListHolder<Transferencia> pageHolder = new PagedListHolder<>(lista);
		pageHolder.setPageSize(4);
		pageHolder.setPage(pageNumber);
		
        List<Transferencia> elements = pageHolder.getPageList();
        int page = pageHolder.getPage();
        int pageSize = pageHolder.getPageSize();
        long totalElements = pageHolder.getNrOfElements();

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Transferencia> p = new PageImpl<>(elements, pageRequest, totalElements);
        
        PageDTO<Transferencia> pdto = new PageDTO<>();
        pdto.setContent(p.getContent());
        pdto.setTotalPages(p.getTotalPages());
        pdto.setTotalElements(p.getTotalElements());
        
        List<Transferencia> allTranferencias = transferenciaRepository.findAll();
        BigDecimal somaTotal = allTranferencias.stream().map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        pdto.setSomaTotal(somaTotal);
        
        BigDecimal somaPeriodo = lista.stream().map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        pdto.setSomaPeriodo(somaPeriodo);
        
        return pdto;
	}
	
	private LocalDateTime validaData(String data, String reg){
		if(!data.matches(reg)) {
			throw new IllegalArgumentException();
		}
		String data2 = data.replaceAll("/", "-") ;
		return LocalDateTime.parse(data2 + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	
	private String validaNome(String nome, String reg){
		if(!nome.matches(reg)) {
			throw new IllegalArgumentException();
		}
		return nome;
	}

}
