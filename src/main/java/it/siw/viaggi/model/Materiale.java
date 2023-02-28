package it.siw.viaggi.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Materiale {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;	
	
	@NotBlank
	private String descrizione;
	
	@NotNull
	@Min(1)
	@Max(1000)
	private int prezzo;
	
	@Column(nullable = true, length = 64)
	private String photos; 
	
	@Transient
	public String photoImagePath;
	public String getPhotosImagePath() {
		if (photos == null || id == null) return null;

		return "/user-photos/" + id + "/" + photos;
	}

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

	public int getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}
	
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String immagine) {
		this.photos = immagine;
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
