package ma.cdgp.af.dto.af;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author elb
 *
 */
@JsonInclude
public class ResponseDTO<T> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T response;
	private String statusCode;
	private String statusType;
	private String statusLabel = null;
	private String statusLabelAr = null;
	private String statusLongLabel = null;
	private String statusLongLabelAr = null;

	public ResponseDTO(T response, String statusCode,String statusType, String statusLabel, String statusLabelAr, 
			String statusLongLabel, String statusLongLabelAr) {
		super();
		this.response = response;
		this.statusCode = statusCode;
		this.statusType = statusType;
		this.statusLabel = statusLabel;
		this.statusLabelAr = statusLabelAr;
		this.statusLongLabel = statusLongLabel;
		this.statusLongLabelAr = statusLongLabelAr;
	}


	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getStatusLabelAr() {
		return statusLabelAr;
	}

	public void setStatusLabelAr(String statusLabelAr) {
		this.statusLabelAr = statusLabelAr;
	}


	public String getStatusType() {
		return statusType;
	}


	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}


	public String getStatusLongLabel() {
		return statusLongLabel;
	}


	public void setStatusLongLabel(String statusLongLabel) {
		this.statusLongLabel = statusLongLabel;
	}


	public String getStatusLongLabelAr() {
		return statusLongLabelAr;
	}


	public void setStatusLongLabelAr(String statusLongLabelAr) {
		this.statusLongLabelAr = statusLongLabelAr;
	}

	
}
