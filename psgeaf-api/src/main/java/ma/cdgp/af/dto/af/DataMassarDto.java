package ma.cdgp.af.dto.af;

public class DataMassarDto {

	private MassarEleveDto eleveData;

	
 
	public DataMassarDto() {
		super();
	}

	public MassarEleveDto getEleveData() {
		return eleveData;
	}

	public void setEleveData(MassarEleveDto eleveData) {
		this.eleveData = eleveData;
	}

	public DataMassarDto(MassarEleveDto eleveData) {
		super();
		this.eleveData = eleveData;
	}
	
}
