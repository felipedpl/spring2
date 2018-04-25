package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casadocodigo.loja.models.Usuario;

@Repository
public class UsuarioDAO implements UserDetailsService {

	@PersistenceContext
	private EntityManager em;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		List<Usuario> users = em.createQuery("select u from Usuario u where u.email = :email", Usuario.class)
				.setParameter("email",email)
				.getResultList();
		
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("O usuario " + email + " nao foi localizado");
		}
		
		return users.get(0);
	}

	public void gravar(Usuario usuario) {
		em.persist(usuario);
	}
}
