package it.siw.viaggi.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Attivita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String descrizione;
	
	@Min(0)
	@Max(9)
	@NotBlank
	private int durata;
	
	@ManyToMany
	private List<Materiale> materiali;
	
	@ManyToMany(mappedBy="attivita")
	private List<Viaggio> viaggi;
	

	public Attivita() {
		super();
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

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public List<Materiale> getMateriali() {
		return materiali;
	}

	public void setMateriali(List<Materiale> materiali) {
		this.materiali = materiali;
	}

	public List<Viaggio> getViaggi() {
		return viaggi;
	}

	public void setViaggi(List<Viaggio> viaggi) {
		this.viaggi = viaggi;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione, durata, id, materiali, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attivita other = (Attivita) obj;
		return Objects.equals(descrizione, other.descrizione) && durata == other.durata && Objects.equals(id, other.id)
				&& Objects.equals(materiali, other.materiali) && Objects.equals(nome, other.nome);
	}
	

}
