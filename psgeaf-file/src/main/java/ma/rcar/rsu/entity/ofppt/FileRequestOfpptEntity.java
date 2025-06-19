package ma.rcar.rsu.entity.ofppt;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.dto.mesrsi.FileRequestMesrsiDto;
import ma.rcar.rsu.dto.ofppt.FileRequestOfpptDto;
import ma.rcar.rsu.entity.mesrsi.DetailsFileRequestMesrsiEntity;
import ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity;


import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author BAKHALED Ibrahim.
 *
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "FILE_REQUEST_OFPPT")
public class FileRequestOfpptEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "DATE_RECEPTION")
    private Date dateReception;

    @Column(name = "PARTENAIRE")
    private String partenaire;

    @Column(name = "TYPE_FILE")
    private String typeFile;

    @Column(name = "DATE_FILE")
    private String dateFile;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailsFileRequestOfpptEntity> lignes;

    @Column(name = "DATE_EVENT")
    private Date dateInsert;

    @Column(name = "OUT_FILE_NAME")
    private String outboundFileName;


    public void beforeSave() {
        if (getLignes() != null) {
            getLignes().forEach(t -> t.setFileRequest(this));
        }
    }

    @PrePersist
    public void prePErsist() {
        setDateInsert(Calendar.getInstance().getTime());
    }

    public static FileRequestOfpptEntity from(FileRequestOfpptDto in) {
        if (in == null)
            return null;
        FileRequestOfpptEntity out = new FileRequestOfpptEntity();
        out.setDateFile(in.getDateFile());
        out.setDateReception(in.getDateReception());
        out.setFileName(in.getFileName());
        out.setPartenaire(in.getPartenaire());
        out.setTypeFile(in.getTypeFile());
        out.setOutboundFileName(in.getOutboundFileName());
        if (in.getDetails() != null) {
            out.setLignes(in.getDetails().stream().map(DetailsFileRequestOfpptEntity::from).collect(Collectors.toList()));
        }
        return out;
    }
}
