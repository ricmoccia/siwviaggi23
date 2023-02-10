package it.siw.viaggi.model;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

public class Materiale {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;	
	
	@NotBlank
	private String descrizione;
	
	@NotBlank
	private float prezzo;

	public Materiale() {
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

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione, id, nome, prezzo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Materiale other = (Materiale) obj;
		return Objects.equals(descrizione, other.descrizione) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome)
				&& Float.floatToIntBits(prezzo) == Float.floatToIntBits(other.prezzo);
	}
	


}
