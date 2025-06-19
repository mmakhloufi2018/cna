package ma.cdgep.paiement.dto;

public class ReponseSyntheseDto {
	
	private SyntheseDto syntheseDto;
	private ErreurDto erreurDto;
	
	public ReponseSyntheseDto(SyntheseDto syntheseDto, ErreurDto erreurDto) {
		super();
		this.syntheseDto = syntheseDto;
		this.erreurDto = erreurDto;
	}

	public SyntheseDto getSyntheseDto() {
		return syntheseDto;
	}

	public void setSyntheseDto(SyntheseDto syntheseDto) {
		this.syntheseDto = syntheseDto;
	}

	public ErreurDto getErreurDto() {
		return erreurDto;
	}

	public void setErreurDto(ErreurDto erreurDto) {
		this.erreurDto = erreurDto;
	}
	
	
	

}
