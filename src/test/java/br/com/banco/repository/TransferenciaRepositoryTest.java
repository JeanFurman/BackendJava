package br.com.banco.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;
import br.com.banco.util.TransferenciaUtil;

@DataJpaTest
class TransferenciaRepositoryTest {

	@Autowired
	private TransferenciaRepository transferenciaRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Test
	void listar_ListandoTodasAsTransferenciasDoBanco() {
		Conta conta = TransferenciaUtil.criarConta();
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(conta);
		Conta contaSalva = contaRepository.save(conta);
		
		List<Transferencia> transferencias = transferenciaRepository.findAll();
		
		Assertions.assertThat(transferencias).isNotEmpty();
		Assertions.assertThat(transferencias).contains(transferencia);
	}
	
	@Test
	void listar_ListandoTodasAsTransferenciasDoContaIdDoBanco() {
		Conta conta = TransferenciaUtil.criarConta();
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(conta);
		Conta contaSalva = contaRepository.save(conta);
		
		List<Transferencia> transferencias = transferenciaRepository.findByContaId(contaSalva);
		
		Assertions.assertThat(transferencias).isNotEmpty();
		Assertions.assertThat(transferencias).contains(transferencia);
		Assertions.assertThat(transferencias.get(0).getContaId()).isEqualTo(transferencia.getContaId());
	}
	
	@Test
	void listar_ListandoTodasAsTransferenciasDoNomeDoOperadorDeTransacaoDoBanco() {
		Conta conta = TransferenciaUtil.criarConta();
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(conta);
		Conta contaSalva = contaRepository.save(conta);
		
		List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao(transferencia.getNomeOperadorTransacao());
		
		Assertions.assertThat(transferencias).isNotEmpty();
		Assertions.assertThat(transferencias).contains(transferencia);
		Assertions.assertThat(transferencias.get(0).getNomeOperadorTransacao()).isEqualTo(transferencia.getNomeOperadorTransacao());
	}
	
	@Test
	void listar_ListandoTodasAsTransferenciasDoNomeDoOperadorDeTransacaoErrado_() {
		List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacao("Sicrano");
		Assertions.assertThat(transferencias).isEmpty();
	}
	
}
