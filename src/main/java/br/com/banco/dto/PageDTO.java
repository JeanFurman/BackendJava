package br.com.banco.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PageDTO<T> {
	
	private List<T> content;
	
	private long totalElements;
	
	private int totalPages;
	
	private BigDecimal somaTotal;
	
	private BigDecimal somaPeriodo;
	
}
