package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import jakarta.transaction.Transactional;

@Service
public class CredentialsService {
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	 @Autowired
	    protected PasswordEncoder passwordEncoder;
	 
	 
	 @Transactional
	    public Credentials getCredentials(Long id) {
	        Optional<Credentials> result = this.credentialsRepository.findById(id);
	        return result.orElse(null);
	    }

	    @Transactional
	    public Credentials getCredentials(String username) {
	        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
	        return result.orElse(null);
	    }
	    
	    @Transactional
	    public Credentials findByUser(User u) {
	        Optional<Credentials> result = this.credentialsRepository.findByUser(u);
	        return result.orElse(null);
	    }
	
	
	@Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

	@Transactional
	public void delete(Long id) {
		this.credentialsRepository.deleteById(id);
		
	}

	@Transactional
	public boolean existByUsername(String username) {
		
		return this.credentialsRepository.existsByUsername(username);
	}

}
