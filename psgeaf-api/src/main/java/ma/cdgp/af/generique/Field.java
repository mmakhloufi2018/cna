package ma.cdgp.af.generique;

import com.google.common.base.CaseFormat;

public class Field {

	private String fieldName;

	private String fieldType;

	public Field() {
		super();
	}

	public Field(String fieldName, String fieldType) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getGetterAndSetterField() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
	}

	public String getTableField() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName);
	}
}
