package it.siw.viaggi.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import it.siw.viaggi.model.User;

@Entity
public class Viaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;		
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String descrizione;
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date partenza;
	//private LocalDate partenza;
	
	@NotNull
	private int durata;
	
	@ManyToMany
	private List<User> viaggiatori;
	
	@ManyToMany
	private List<Attivita> Attivita;
	
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

	public Date getPartenza() {
		return partenza;
	}

	public void setPartenza(Date partenza) {
		this.partenza = partenza;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public List<Attivita> getAttivita() {
		return Attivita;
	}

	public void setAttivita(List<Attivita> Attivita) {
		this.Attivita = Attivita;
	}

	public Guida getGuida() {
		return guida;
	}

	public void setGuida(Guida guida) {
		this.guida = guida;
	}
	
	public List<User> getViaggiatori() {
		return this.viaggiatori;
	}

	public void setViaggiatori(List<User> viaggiatori) {
		this.viaggiatori = viaggiatori;
	}
	
	public boolean addViaggiatore(User user) {
		return this.viaggiatori.add(user);
	}

	public boolean rimuoviViaggiatore(User user) {
		return this.viaggiatori.remove(user);
	}
	
	public boolean partecipa(User user) {
		if(this.viaggiatori==null) return false;
		return this.viaggiatori.contains(user);
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
