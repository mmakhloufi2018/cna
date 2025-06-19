package ma.cdgep.demande.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BLOC")
public class BlocEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "NOMBRE_TRANSACTION")
	private String nombreTransaction;
	@Column(name = "DATE_BLOC")
	private String dateBloc;
	@Column(name = "HASH")
	private String hash;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreTransaction() {
		return nombreTransaction;
	}

	public void setNombreTransaction(String nombreTransaction) {
		this.nombreTransaction = nombreTransaction;
	}

	public String getDateBloc() {
		return dateBloc;
	}

	public void setDateBloc(String dateBloc) {
		this.dateBloc = dateBloc;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public BlocEntity(String nombreTransaction, String dateBloc, String hash) {
		super();
		this.nombreTransaction = nombreTransaction;
		this.dateBloc = dateBloc;
		this.hash = hash;
	}
	
	

}
