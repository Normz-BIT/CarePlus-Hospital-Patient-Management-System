package com.careplus.common.model;

import java.util.List;

import com.careplus.common.enums.UserRole;

<<<<<<< HEAD
/*
 * Receptionist is the front desk staff type and the triage point of our
 * complaint workflow. Receptionists are the only role that assigns a complaint
 * to a doctor or nurse, which is the step that moves it from SUBMITTED to
 * ASSIGNED. Keeping that permission to one role is what stops two members of
 * staff picking up the same complaint.
 *
 * TODO: add the JPA mapping for the staff subclasses, as described on Doctor.
 */
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "receptionist")
@PrimaryKeyJoinColumn(name = "person_id")
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
public class Receptionist extends Employee {

<<<<<<< HEAD
	/*
	 * Desk number is a String rather than an int because desks are labelled
	 * rather than numbered in sequence, so values like "A2" need to be valid.
	 */
	private String deskNo;
	private List<Complaint> complaintsList;
=======
    @Transient
    private static final long serialVersionUID = 1L;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	/*
	 * As with the other staff types, the role is fixed here so a Receptionist
	 * cannot be built carrying the wrong UserRole.
	 */
	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}
=======
    @Column(name = "desk_no", length = 20)
    private String deskNo;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
<<<<<<< HEAD
	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password,
			List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST, complaint);
		// TODO Auto-generated constructor stub
=======
	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST);
>>>>>>> stash
	}
=======
    public Receptionist() {
        super();
        setRole(UserRole.RECEPTIONIST);
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    public Receptionist(String personId, String firstName, String lastName,
                        String email, String phone, String password) {
        super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST);
    }

    public String getDeskNo() {
        return deskNo;
    }

<<<<<<< HEAD
	public List<Complaint> getComplaintsList() {
		return complaintsList;
	}

	public void setComplaintsList(List<Complaint> complaintsList) {
		this.complaintsList = complaintsList;
	}
=======
    public void setDeskNo(String deskNo) {
        this.deskNo = deskNo;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    @Override
    public String toString() {
        return "Receptionist [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", department=" + department
                + ", hireDate=" + hireDate
                + ", deskNo=" + deskNo + "]";
    }
}