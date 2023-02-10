package it.siw.viaggi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

public class Viaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;		
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String descrizione;
	
	@NotBlank
	private LocalDate partenza;
	
	@NotBlank
	private int durata;
	
	//@ManyToMany
	//private Set<User> participants;
	
	@ManyToMany
	private List<Attivita> attivita;
	
	@ManyToOne
	private Guida guida;

	public Viaggio() {
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

	public LocalDate getPartenza() {
		return partenza;
	}

	public void setPartenza(LocalDate partenza) {
		this.partenza = partenza;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public List<Attivita> getAttivita() {
		return attivita;
	}

	public void setAttivita(List<Attivita> attivita) {
		this.attivita = attivita;
	}

	public Guida getGuida() {
		return guida;
	}

	public void setGuida(Guida guida) {
		this.guida = guida;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(descrizione, durata, guida, id, nome, partenza);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viaggio other = (Viaggio) obj;
		return Objects.equals(descrizione, other.descrizione) && durata == other.durata
				&& Objects.equals(guida, other.guida) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(partenza, other.partenza);
	}

	

}
