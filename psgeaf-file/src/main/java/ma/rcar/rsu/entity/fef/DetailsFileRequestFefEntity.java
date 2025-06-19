package ma.rcar.rsu.entity.fef;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ma.rcar.rsu.dto.fef.DetailsFileRequestFefDto;
import org.apache.commons.beanutils.BeanUtils;


import jakarta.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Entity(name = "DETAILS_FILE_REQUEST_FEF")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestFefEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "CIN")
    private String cin;
    @Column(name = "DATE_PEC")
    private String datePec;
    @Column(name = "DATE_EXPIRATION")
    private String dateExpiration;
    @ManyToOne
    @JoinColumn(name = "FILE_REQUEST_FEF_ID", nullable = false)
    private FileRequestFefEntity fileRequest;



    public static DetailsFileRequestFefEntity from(DetailsFileRequestFefDto in) {
        if (in == null)
            return null;
        DetailsFileRequestFefEntity out = new DetailsFileRequestFefEntity();
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
