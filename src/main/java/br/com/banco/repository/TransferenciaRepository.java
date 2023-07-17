package br.com.banco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;


public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>{
	List<Transferencia> findByContaId(Conta contaId);
	List<Transferencia> findByNomeOperadorTransacao(String nomeOperadorTransacao);
}
