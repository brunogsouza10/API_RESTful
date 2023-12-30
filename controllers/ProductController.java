package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dto.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("unused")
@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository; // ponto de injecao de dependencias quando necessarias

	// método get all

	@GetMapping("/tb_products")
	public ResponseEntity<List<ProductModel>> getAllProducts() {
		List<ProductModel> productsList = productRepository.findAll();
		if (!productsList.isEmpty()) {
			for(ProductModel product: productsList) {
				UUID id = product.getIdProduct();
				product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(productsList);
	}

	
	// metodo get one

	@GetMapping("/tb_products/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
		Optional<ProductModel> product0 = productRepository.findById(id);
		if (product0.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
			
		}
		product0.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
		return ResponseEntity.status(HttpStatus.OK).body(product0.get());

	}


	// MÉTODO POST
	@PostMapping("/tb_products")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));

		// Esse método POST vai realizar a conversao dos dados DTO para MODEL,
		// assim todo novo produto criado nas requisicoes será transf para o banco de
		// dados.
	}

	// Método DELETE
	@DeleteMapping("/tb_products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
		Optional<ProductModel> product0 = productRepository.findById(id);
		if (product0.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		productRepository.delete(product0.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
	}

	// Método PUT
	@PutMapping("/tb_products/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
			@RequestBody @Valid ProductRecordDto productRecordDto) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		var productModel = productO.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
	}

}
