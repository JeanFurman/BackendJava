package br.com.banco.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;

public class TransferenciaUtil {
	
	public static Conta criarConta() {
		Conta c1 = new Conta();
		c1.setNomeResponsavel("Fulano");
		return c1;
	}
	
	public static Transferencia criarTransferencia(Conta c1) {
		
		Transferencia t1 = new Transferencia();
		t1.setDataTransferencia(LocalDateTime.parse("01-01-2019 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")));
		t1.setValor(BigDecimal.valueOf(30895.46));
		t1.setTipo("Depósito");
		t1.setContaId(c1);
		t1.setNomeOperadorTransacao("Beltrano");
		
		c1.getTransferencias().add(t1);
		
		return t1;
	}
	
	public static List<Transferencia> criarListaDeTransferencias(Conta c1) {
		
		Transferencia t1 = new Transferencia();
		t1.setDataTransferencia(LocalDateTime.parse("01-01-2019 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")));
		t1.setValor(BigDecimal.valueOf(30895.46));
		t1.setTipo("Depósito");
		t1.setContaId(c1);
		t1.setNomeOperadorTransacao("Beltrano");
		
		c1.getTransferencias().add(t1);
		
		Transferencia t2 = new Transferencia();
		t2.setDataTransferencia(LocalDateTime.parse("01-01-2020 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")));
		t2.setValor(BigDecimal.valueOf(30895.46));
		t2.setTipo("Depósito");
		t2.setContaId(c1);
		t1.setNomeOperadorTransacao("Beltrano");
		
		c1.getTransferencias().add(t2);
		
		return List.of(t1, t2);
	}

}
