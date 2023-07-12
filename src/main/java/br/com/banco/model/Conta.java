package br.com.banco.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id_conta;
	
	@Column(length = 50, nullable = false)
	private String nome_responsavel;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "conta")
	private List<Transferencia> transferencias = new ArrayList<>();
	
}
