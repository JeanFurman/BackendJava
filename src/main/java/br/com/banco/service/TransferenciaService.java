package br.com.banco.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
	public List<Transferencia> buscarTransferenciasPorIdConta(Long id) {
		return contaRepository.findById(id)
				.map(c -> transferenciaRepository.findByContaId(c))
				.orElseThrow(() -> new RuntimeException("Conta not found"));
	}
	
	public List<Transferencia> buscarTransferenciasPorNomeOperadorTransacao(String nome) {
		return transferenciaRepository.findByNomeOperadorTransacao(nome);
	}
	
}
