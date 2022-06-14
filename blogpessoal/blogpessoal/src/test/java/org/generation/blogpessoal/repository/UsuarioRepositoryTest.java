package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void Start() {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario(0L, "João da Silva", "https://i.imgur.com/FETvs20.jpg", "joao@gmail.com", "123456789"));
		usuarioRepository.save(new Usuario(0L, "Robertinho Carvalho", "https://i.imgur.com/JR7kUFU.jpg", "robertinho@gmail.com", "123456789"));
		usuarioRepository.save(new Usuario(0L, "Angela das Luzes", "https://i.imgur.com/NtyGneo.jpg", "angela@gmail.com", "123456789"));
		usuarioRepository.save(new Usuario(0L, "Elisa Lara", "https://i.imgur.com/mB3VM2N.jpg", "elisa@gmail.com", "123456789"));
	}
	
	@Test
	@DisplayName("Retorna 1 usuário")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("joao@gmail.com"));
	}
	
	@Test
	@DisplayName("Retorna 3 usuários")
	public void deveRetornarTresUsuarios() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Robertinho Carvalho"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Angela das Luzes"));
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}
}
