package br.com.banco.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;
import br.com.banco.util.TransferenciaUtil;

@DataJpaTest
class ContaRepositoryTest {
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Test
	void salvar_SalvandoContaNoBanco_BemSucedido() {
		Conta conta = TransferenciaUtil.criarConta();
		Conta contaSalva = contaRepository.save(conta);
		
		Assertions.assertThat(contaSalva).isNotNull();
		Assertions.assertThat(contaSalva.getIdConta()).isNotNull();
		Assertions.assertThat(contaSalva.getNomeResponsavel()).isEqualTo(conta.getNomeResponsavel());
		
	}
	
	@Test
	void salvar_SalvandoTransferenciaNoBanco_BemSucedido() {
		Conta conta = TransferenciaUtil.criarConta();
		Transferencia transferencia = TransferenciaUtil.criarTransferencia(conta);
		Conta contaSalva = contaRepository.save(conta);
		
		Assertions.assertThat(contaSalva.getTransferencias()).isNotEmpty();
		Assertions.assertThat(contaSalva.getTransferencias().contains(transferencia));
		
	}

}
