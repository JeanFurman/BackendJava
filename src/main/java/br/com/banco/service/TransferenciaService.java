package br.com.banco.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.banco.dto.PageDTO;
import br.com.banco.model.Transferencia;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;

@Service
public class TransferenciaService {
	private final ContaRepository contaRepository;
	private final TransferenciaRepository transferenciaRepository;

	public TransferenciaService(ContaRepository contaRepository, TransferenciaRepository transferenciaRepository) {
		this.contaRepository = contaRepository;
		this.transferenciaRepository = transferenciaRepository;
	}

	public List<Transferencia> listarTodasAsTransferencias(){
		return transferenciaRepository.findAll();
	}
	
	public PageDTO<Transferencia> buscarTransferenciasPorIdConta(Pageable pageable, Long id) {
		List<Transferencia> l = contaRepository.findById(id)
				.map(c -> transferenciaRepository.findByContaId(c))
				.orElseThrow(() -> new RuntimeException("Conta not found"));
		return fazPaginas(pageable.getPageNumber(), l);
		
	}
	
	public List<Transferencia> buscarTransferenciasPorNomeOperadorTransacao(String nome) {
		return transferenciaRepository.findByNomeOperadorTransacao(nome);
	}
	
	public PageDTO<Transferencia> fazPaginas(int pageNumber, List<Transferencia> lista){
		PagedListHolder<Transferencia> pageHolder = new PagedListHolder<>(lista);
		pageHolder.setPageSize(4);
		pageHolder.setPage(pageNumber);
		
        List<Transferencia> elements = pageHolder.getPageList();
        int page = pageHolder.getPage();
        int pageSize = pageHolder.getPageSize();
        long totalElements = pageHolder.getNrOfElements();

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page p = new PageImpl<>(elements, pageRequest, totalElements);
        
        PageDTO<Transferencia> pdto = new PageDTO();
        pdto.setContent(p.getContent());
        pdto.setTotalPages(p.getTotalPages());
        pdto.setTotalElements(p.getTotalElements());
        
        List<Transferencia> allTranferencias = listarTodasAsTransferencias();
        BigDecimal somaTotal = allTranferencias.stream().map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        pdto.setSomaTotal(somaTotal);
        
        BigDecimal somaPeriodo = lista.stream().map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        pdto.setSomaPeriodo(somaPeriodo);
        
        return pdto;
	}
	
	public LocalDateTime validaData(String data, String reg){
		if(!data.matches(reg)) {
			throw new IllegalArgumentException();
		}
		String data2 = data.replaceAll("/", "-") ;
		return LocalDateTime.parse(data2 + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	
	public String validaNome(String nome, String reg){
		if(!nome.matches(reg)) {
			throw new IllegalArgumentException();
		}
		return nome;
	}
	
}
