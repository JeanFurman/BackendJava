package br.com.banco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.banco.model.Conta;
import br.com.banco.model.Transferencia;
import br.com.banco.repository.ContaRepository;

@SpringBootApplication
public class BancoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoApplication.class, args);
    }
    
    @Bean
	CommandLineRunner initDatabase(ContaRepository contaRepository) {
		return args -> {
			contaRepository.deleteAll();
			
			Conta c1 = new Conta();
			c1.setNomeResponsavel("Fulano");
			
			Transferencia t1 = new Transferencia();
			t1.setDataTransferencia(LocalDateTime.parse("2019-01-01 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t1.setValor(BigDecimal.valueOf(30895.46));
			t1.setTipo("DEPOSITO");
			t1.setContaId(c1);
			
			c1.getTransferencias().add(t1);
			
			Transferencia t2 = new Transferencia();
			t2.setDataTransferencia(LocalDateTime.parse("2019-05-04 08:12:45", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t2.setValor(BigDecimal.valueOf(-500.50));
			t2.setTipo("SAQUE");
			t2.setContaId(c1);
			
			c1.getTransferencias().add(t2);
			
			Transferencia t3 = new Transferencia();
			t3.setDataTransferencia(LocalDateTime.parse("2020-06-08 10:15:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t3.setValor(BigDecimal.valueOf(3241.23));
			t3.setTipo("TRANSFERENCIA");
			t3.setNomeOperadorTransacao("Beltrano");
			t3.setContaId(c1);
			
			c1.getTransferencias().add(t3);
			
			Transferencia t20 = new Transferencia();
			t20.setDataTransferencia(LocalDateTime.parse("2022-06-08 10:15:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t20.setValor(BigDecimal.valueOf(3241.23));
			t20.setTipo("TRANSFERENCIA");
			t20.setNomeOperadorTransacao("Beltrano");
			t20.setContaId(c1);
			
			c1.getTransferencias().add(t20);
			
			Conta c2 = new Conta();
			c2.setNomeResponsavel("Sicrano");
			
			Transferencia t4 = new Transferencia();
			t4.setDataTransferencia(LocalDateTime.parse("2019-02-03 09:53:27", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t4.setValor(BigDecimal.valueOf(12.24));
			t4.setTipo("DEPOSITO");
			t4.setContaId(c2);
			
			Transferencia t5 = new Transferencia();
			t5.setDataTransferencia(LocalDateTime.parse("2019-08-07 08:12:45", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t5.setValor(BigDecimal.valueOf(-530.50));
			t5.setTipo("SAQUE");
			t5.setContaId(c2);
			
			Transferencia t6 = new Transferencia();
			t6.setDataTransferencia(LocalDateTime.parse("2021-04-01 12:12:04", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			t6.setValor(BigDecimal.valueOf(25173.09));
			t6.setTipo("TRANSFERENCIA");
			t6.setNomeOperadorTransacao("Ronnyscley");
			t6.setContaId(c2);
			
			c2.getTransferencias().add(t4);
			c2.getTransferencias().add(t5);
			c2.getTransferencias().add(t6);
			
			
			contaRepository.save(c1);
			contaRepository.save(c2);
		};
	}
}
