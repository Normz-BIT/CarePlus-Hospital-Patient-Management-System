package com.careplus.common.model;

import java.io.Serializable;
import java.util.List;

import com.careplus.common.enums.NurseWard;

/*
 * Child of the Employee and Person Class
 * 
 */
public class Nurse extends Employee{

	private static final long serialVersionUID = 1L;
	private NurseWard ward;
	private List<VitalSigns> vitalSignsList;
	
	public Nurse() {
		
	}

	public NurseWard getWard() {
		return ward;
	}

	public void setWard(NurseWard ward) {
		this.ward = ward;
	}

	public List<VitalSigns> getVitalSignsList() {
		return vitalSignsList;
	}

	public void setVitalSignsList(List<VitalSigns> vitalSignsList) {
		this.vitalSignsList = vitalSignsList;
	}

}
