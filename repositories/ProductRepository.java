package com.example.springboot.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository< ProductModel, UUID> {
	
	//A interface foi anotada com @Repository para mostrar ao Spring que 
	//essa será um bean gerenciado por ele e como tal interface tem como objetivo transações 
	//com o banco de dados o melhor estereótipo a ser utilizado neste caso é @Repository.


}
