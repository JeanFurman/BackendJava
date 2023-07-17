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
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_conta")
	private Long idConta;
	
	@NotNull
	@Column(length = 50, nullable = false, name = "nome_responsavel")
	private String nomeResponsavel;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "contaId")
	private List<Transferencia> transferencias = new ArrayList<>();
	
}
