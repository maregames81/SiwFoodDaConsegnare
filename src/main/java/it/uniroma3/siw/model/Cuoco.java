package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;


@JsonIdentityInfo(
		   generator = ObjectIdGenerators.PropertyGenerator.class, 
		   property = "id")
@Entity
public class Cuoco {
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
	
	
	private String nome;
	
	
	private String cognome;
	
	
	@Column(nullable=false)
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate nascita;
	
	@JsonIgnore
	@Lob
	@Column(columnDefinition = "TEXT")
	private String immagine;
	
	@JsonIgnore
	@OneToMany(mappedBy="cuoco",cascade = {CascadeType.REMOVE})
	private List<Ricetta> ricette;
	

	public Cuoco() {
		this.ricette= new LinkedList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getNascita() {
		return nascita;
	}

	public void setNascita(LocalDate nascita) {
		this.nascita = nascita;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public List<Ricetta> getRicette() {
		return ricette;
	}

	public void setRicette(List<Ricetta> ricette) {
		this.ricette = ricette;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, id, immagine, nascita, nome, ricette);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuoco other = (Cuoco) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(id, other.id)
				&& Objects.equals(immagine, other.immagine) && Objects.equals(nascita, other.nascita)
				&& Objects.equals(nome, other.nome) && Objects.equals(ricette, other.ricette);
	}
	
	
	

}
