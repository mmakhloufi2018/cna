package ma.rcar.rsu.entity.ofppt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.dto.ofppt.DetailsFileRequestOfpptDto;
import ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity;
import org.apache.commons.beanutils.BeanUtils;

import jakarta.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Entity(name = "DETAILS_FILE_REQUEST_OFPPT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestOfpptEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "NUM_LOT")
    private String numLot;

    @Column(name ="CEF")
    private String cef;

    @Column(name = "NUM_MASSAR")
    private String numMassar;

    @Column(name = "CNIE")
    private String cnie;

    @Column(name = "DATE_NAISSANCE")
    private String dateNaissance;

    @Column(name = "NOM")
    private String nom;

    @Column(name = "PRENOM")
    private String prenom;

    @Column(name = "ANNEE_SCOLAIRE")
    private String anneeScolaire;


    @Column(name = "BOURSIER")
    private String boursier;


    @Column(name= "MONTANT")
    private String montant;


    @Column(name = "ETAT")
    private String etat;

    @Column(name = "MOTIF")
    private String motif;

    @ManyToOne
    @JoinColumn(name = "FILE_REQUEST_OFPPT_ID", nullable = false)
    private FileRequestOfpptEntity fileRequest;

    @Column(name = "RANG")
    private Integer rang;

    @Column(name = "CODE_MOTIF")
    private String codeMotif;


    public static DetailsFileRequestOfpptEntity from(DetailsFileRequestOfpptDto in) {
        if (in == null)
            return null;
        DetailsFileRequestOfpptEntity out = new DetailsFileRequestOfpptEntity();
        try {
            BeanUtils.copyProperties(out, in);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return out;
    }
}
