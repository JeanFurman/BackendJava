package br.com.banco.controller;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.dto.PageDTO;
import br.com.banco.model.Transferencia;
import br.com.banco.service.TransferenciaService;
import br.com.banco.util.TransferenciaUtil;

@ExtendWith(SpringExtension.class)
public class TransferenciaControllerTest {

	@InjectMocks
	private TransferenciaController transferenciaController;
	
	@Mock
	private TransferenciaService transferenciaServiceMock;
	
	@BeforeEach
	void setUp() {
		
		List<Transferencia> transferenciaList = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		
        BDDMockito.when(transferenciaServiceMock.buscarTransferenciasPorIdConta(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyLong()))
        			.thenReturn(geradorPageDTO(transferenciaList));
        
        BDDMockito.when(transferenciaServiceMock.buscarTransferenciasPorNomeOperadorTransacao(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString()))
					.thenReturn(geradorPageDTO(transferenciaList));
        
        BDDMockito.when(transferenciaServiceMock.buscarTransferenciasPorNomeOperadorTransacaoEPorPeriodo(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
					.thenReturn(geradorPageDTO(transferenciaList));
        
        BDDMockito.when(transferenciaServiceMock.buscarTransferenciasPorPeriodo(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
					.thenReturn(geradorPageDTO(transferenciaList));
        
        BDDMockito.when(transferenciaServiceMock.listarTodasAsTransferencias(ArgumentMatchers.any(Pageable.class)))
					.thenReturn(geradorPageDTO(transferenciaList));
        
        
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorIdDaContaDentroDeUmPageDTO(){
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(TransferenciaUtil.criarConta());
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.buscarTransferenciasPorIdConta(pageable, 1L);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getContaId().getNomeResponsavel()).isEqualTo(transferencia.getContaId().getNomeResponsavel());
	}
	
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorNomeDoOperadorDaTransferenciaDentroDeUmPageDTO(){
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(TransferenciaUtil.criarConta());
		String nomeEsperado = transferencia.getNomeOperadorTransacao();
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.filtroParaAsTransferencias(pageable, nomeEsperado, "", "");
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getNomeOperadorTransacao()).isEqualTo(nomeEsperado);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorNomeDoOperadorPorDataInicioEDataFimDentroDeUmPageDTO(){
		List<Transferencia> transferencias = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		String dataInicio = transferencias.get(0).getDataTransferencia().toString();
		String dataFim = transferencias.get(1).getDataTransferencia().toString();
		String nomeEsperado = transferencias.get(0).getNomeOperadorTransacao();
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.filtroParaAsTransferencias(pageable, nomeEsperado, dataInicio, dataFim);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getDataTransferencia()).isEqualTo(dataInicio);
		Assertions.assertThat(pages.getContent().get(0).getNomeOperadorTransacao()).isEqualTo(nomeEsperado);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorDataInicioEDataFimDentroDeUmPageDTO(){
		List<Transferencia> transferencias = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		String dataInicio = transferencias.get(0).getDataTransferencia().toString();
		String dataFim = transferencias.get(1).getDataTransferencia().toString();
		
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.filtroParaAsTransferencias(pageable, "", dataInicio, dataFim);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getDataTransferencia()).isEqualTo("2019-01-01T12:00:00");
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasDentroDeUmPageDTO(){
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.filtroParaAsTransferencias(pageable, "", "", "");
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty().hasSize(2);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorDataInicioEDataFimErrados(){
		List<Transferencia> transferencias = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		String dataInicio = transferencias.get(0).getDataTransferencia().toString();
		String dataFim = transferencias.get(1).getDataTransferencia().toString();
		
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaController.filtroParaAsTransferencias(pageable, "", dataInicio, dataFim);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getDataTransferencia()).isEqualTo("2019-01-01T12:00:00");
	}
	
	PageDTO<Transferencia> geradorPageDTO(List<Transferencia> transferencia) {
		PagedListHolder<Transferencia> pageHolder = new PagedListHolder<>(transferencia);
		pageHolder.setPageSize(4);
		pageHolder.setPage(0);
		
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
        pdto.setSomaTotal(BigDecimal.valueOf(30895.46));
        pdto.setSomaPeriodo(BigDecimal.valueOf(30895.46));
        
        return pdto;
	}
	
	
}
