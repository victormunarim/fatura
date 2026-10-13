package com.example.fatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

// informações gerais que aparecem no topo da documentação OpenAPI
@OpenAPIDefinition(info = @Info(
		title = "API Fatura",
		description = "Lê faturas/extratos em PDF, salva as compras no banco e permite consultá-las, ordená-las, filtrá-las e ver a distribuição dos gastos por categoria."))

@SpringBootApplication
public class FaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaturaApplication.class, args);
	}

}
