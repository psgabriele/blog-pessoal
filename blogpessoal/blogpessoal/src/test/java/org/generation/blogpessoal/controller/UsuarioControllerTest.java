package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um usuário")
	public void deveCriarUmUsuario() {
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Elisa Lara", "https://i.imgur.com/mB3VM2N.jpg", "elisa@gmail.com", "123456789"));
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "https://i.imgur.com/T12NIp9.jpg", "maria@gmail.com", "12345678"));
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva", "https://i.imgur.com/T12NIp9.jpg", "maria@gmail.com", "12345678"));
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Atualizar um usuário")
	public void deveAtualizarUmUsuario() {
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andreia", "https://i.imgur.com/yDRVeK7.jpg", "juliana@gmail.com", "juliana123"));
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
				"Juliana Andreia Ramos", "https://i.imgur.com/yDRVeK7.jpg", "juliana_ramos@gmail.com", "juliana123");
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root", "root").exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "https://i.imgur.com/5M2p5Wb.jpg", "sabrina@gmail.com", "sabrina123"));
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "https://i.imgur.com/Sk5SjWE.jpg", "ricardo@gmail.com", "ricardo123"));
		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root", "root").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
