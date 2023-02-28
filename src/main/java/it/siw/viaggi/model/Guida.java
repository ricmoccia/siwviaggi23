package it.siw.viaggi.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Guida {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	private String cognome;

	@NotBlank
	private String nazionalita;
	
	@OneToMany(mappedBy="guida")
	private List<Viaggio> viaggi;
	
	
	public Guida() {
		super();
	}


	@Override
	public int hashCode() {
		return Objects.hash(cognome, id, nazionalita, nome, viaggi);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guida other = (Guida) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(id, other.id)
				&& Objects.equals(nazionalita, other.nazionalita) && Objects.equals(nome, other.nome)
				&& Objects.equals(viaggi, other.viaggi);
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


	public String getNazionalita() {
		return nazionalita;
	}


	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}


	public List<Viaggio> getViaggi() {
		return viaggi;
	}


	public void setViaggi(List<Viaggio> viaggi) {
		this.viaggi = viaggi;
	}


	@Override
	public String toString() {
		return nome+ " " + cognome;
	}
	


}
