package ma.rcar.rsu.dto.fef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

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
public class FileRequestFefDto {
    private String fileName;
    private Date dateReception;
    private String partenaire;
    private String typeFile;
    private String dateFile;
    private String outboundFileName;
    private List<DetailsFileRequestFefDto> details;


    public List<DetailsFileRequestFefDto> getLignesRejet() {
        if (CollectionUtils.isEmpty(getDetails())) {
            return null;
        }
        return getDetails().stream().filter(l -> l.getEtat() != null && l.getEtat().equalsIgnoreCase("KO"))
                .collect(Collectors.toList());
    }

    public List<DetailsFileRequestFefDto> getLignesValides() {
        if (CollectionUtils.isEmpty(getDetails())) {
            return null;
        }
        return getDetails().stream().filter(l -> l.getEtat() != null && l.getEtat().equalsIgnoreCase("OK"))
                .collect(Collectors.toList());
    }
}
