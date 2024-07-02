package it.uniroma3.siw.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@JsonIdentityInfo(
		   generator = ObjectIdGenerators.PropertyGenerator.class, 
		   property = "id")
@Entity
public class Ricetta {

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Column(nullable=false)
	private String nome;

	@Column(length =2000)
	private String descrizione;

	@JsonIgnore
	@Lob
	@Column(columnDefinition = "TEXT")
	private String foto;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cuoco_id")
	private Cuoco cuoco;

	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="ricetta_id")
	private List<QuantitaIngrediente> quantitaIngrediente;
	

	public Ricetta() {
		this.quantitaIngrediente= new LinkedList<>();
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Cuoco getCuoco() {
		return cuoco;
	}

	public void setCuoco(Cuoco cuoco) {
		this.cuoco = cuoco;
	}

	public List<QuantitaIngrediente> getQuantitaIngrediente() {
		return quantitaIngrediente;
	}

	public void setQuantitaIngrediente(List<QuantitaIngrediente> quantitaIngrediente) {
		this.quantitaIngrediente = quantitaIngrediente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cuoco, descrizione, foto, id, nome, quantitaIngrediente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ricetta other = (Ricetta) obj;
		return Objects.equals(cuoco, other.cuoco) && Objects.equals(descrizione, other.descrizione)
				&& Objects.equals(foto, other.foto) && Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(quantitaIngrediente, other.quantitaIngrediente);
	}









}
