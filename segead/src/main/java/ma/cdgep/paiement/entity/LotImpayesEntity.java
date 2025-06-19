package ma.cdgep.paiement.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LOT_IMAPYES")
public class LotImpayesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "NUMERO_LOT")
	private Integer numeroLot;
	@Column(name = "DATE_LOT")
	private Date dateLot;

	@OneToMany(mappedBy = "lotImpayes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ImpayeEntity> impayes;

	public Integer getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(Integer numeroLot) {
		this.numeroLot = numeroLot;
	}

	public Set<ImpayeEntity> getImpayes() {
		return impayes;
	}

	public void setImpayes(Set<ImpayeEntity> impayes) {
		this.impayes = impayes;
	}

	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		this.dateLot = dateLot;
	}

}
