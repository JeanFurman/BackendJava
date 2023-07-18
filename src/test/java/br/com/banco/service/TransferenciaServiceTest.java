package br.com.banco.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.dto.PageDTO;
import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;
import br.com.banco.repository.ContaRepository;
import br.com.banco.repository.TransferenciaRepository;
import br.com.banco.util.TransferenciaUtil;

@ExtendWith(SpringExtension.class)
public class TransferenciaServiceTest {

	@InjectMocks
	private TransferenciaService transferenciaService;
	
	@Mock
	private TransferenciaRepository transferenciaRepositoryMock;
	
	@Mock
	private ContaRepository contaRepositoryMock;
	
	@BeforeEach
	void setUp() {
		
		List<Transferencia> transferenciaList = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		
        BDDMockito.when(transferenciaRepositoryMock.findByContaId(ArgumentMatchers.any(Conta.class)))
        			.thenReturn(transferenciaList);
        
        BDDMockito.when(contaRepositoryMock.findById(ArgumentMatchers.anyLong()))
					.thenReturn(Optional.of(TransferenciaUtil.criarConta()));
        
        BDDMockito.when(transferenciaRepositoryMock.findAll())
					.thenReturn(transferenciaList);
        
        BDDMockito.when(transferenciaRepositoryMock.findByNomeOperadorTransacao(ArgumentMatchers.anyString()))
					.thenReturn(transferenciaList);
        
        
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasDentroDeUmPageDTO(){
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaService.listarTodasAsTransferencias(pageable);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty().hasSize(2);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorIdDaContaDentroDeUmPageDTO(){
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(TransferenciaUtil.criarConta());
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaService.buscarTransferenciasPorIdConta(pageable, 1L);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getContaId().getNomeResponsavel()).isEqualTo(transferencia.getContaId().getNomeResponsavel());
	}
	
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorNomeDoOperadorDaTransferenciaDentroDeUmPageDTO(){
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(TransferenciaUtil.criarConta());
		String nomeEsperado = transferencia.getNomeOperadorTransacao();
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(pageable, nomeEsperado);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getNomeOperadorTransacao()).isEqualTo(nomeEsperado);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorNomeDoOperadorPorDataInicioEDataFimDentroDeUmPageDTO(){
		List<Transferencia> transferencias = TransferenciaUtil.criarListaDeTransferencias(TransferenciaUtil.criarConta());
		String nomeEsperado = transferencias.get(0).getNomeOperadorTransacao();
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaService.buscarTransferenciasPorNomeOperadorTransacaoEPorPeriodo(pageable, "01/01/2019", "01/01/2020", nomeEsperado);
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getDataTransferencia()).isEqualTo("2019-01-01T12:00:00");
		Assertions.assertThat(pages.getContent().get(0).getNomeOperadorTransacao()).isEqualTo(nomeEsperado);
	}
	
	@Test
	void listar_ListagemDeTodosAsTransferenciasPorDataInicioEDataFimDentroDeUmPageDTO(){
		
		Pageable pageable = PageRequest.of(0, 4);
		
		PageDTO<Transferencia> pages = transferenciaService.buscarTransferenciasPorPeriodo(pageable, "01/01/2019", "01/01/2020");
		
		Assertions.assertThat(pages).isNotNull();
		Assertions.assertThat(pages.getContent()).isNotEmpty();
		Assertions.assertThat(pages.getContent().get(0).getDataTransferencia()).isEqualTo("2019-01-01T12:00:00");
	}
	
	@Test
	void listar_DataInicioEDataFimErrados_LançarIllegalArgumentException(){
		
		Pageable pageable = PageRequest.of(0, 4);
		
		Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
		.isThrownBy(() -> this.transferenciaService.buscarTransferenciasPorPeriodo(pageable, "01-01-2019", "01-01-2020"));
	}
	
	@Test
	void listar_NomeDoOperadorErrado_LançarIllegalArgumentException(){
		
		Pageable pageable = PageRequest.of(0, 4);
		
		Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
		.isThrownBy(() -> this.transferenciaService.buscarTransferenciasPorNomeOperadorTransacao(pageable, ""));
	}
	
	
}
