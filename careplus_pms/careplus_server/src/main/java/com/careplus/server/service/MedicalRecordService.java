package com.careplus.server.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.model.MedicalRecord;
import com.careplus.common.model.Patient;
import com.careplus.common.model.VitalSigns;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * MedicalRecordService
 * The clinical side: doctors writing diagnoses and nurses recording vitals, plus
 * the reads that show them back on both dashboards.
 */
public class MedicalRecordService extends BaseService {

	private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);

	/*
	 * Append only here: every diagnosis is a new row so the patient's history keeps
	 * all the old ones. The doctor comes off the request (whoever is signed in),
	 * not from whatever the client put on the record.
	 */
	public Response addDiagnosis(Request request) {

		MedicalRecord record = (MedicalRecord) request.getParams().get("medicalRecord");
		String doctorId = (String) request.getParams().get("doctorId");

		startSession();

		try {

			String patientId = record.getPatientId() == null ? "" : record.getPatientId().trim().toUpperCase();

			/*
			 * The patient ID is typed by hand on the diagnosis screen, so check it's a
			 * real patient before writing anything. Failing here gets the doctor a
			 * readable message instead of a foreign key error at commit time.
			 */
			if (session.find(Patient.class, patientId) == null) {
				throw new IllegalArgumentException("No patient with ID " + patientId);
			}

			record.setPatientId(patientId);
			record.setDoctorId(doctorId);

			session.persist(record);

			resp.setSuccess(true);
			resp.setMessage("Diagnosis saved");

			logger.info("Diagnosis added for patient {} by doctor {}", patientId, doctorId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to save diagnosis: " + e.getMessage());

			logger.error("Could not save diagnosis", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Update only copies across the fields that can actually be edited. We load the
	 * real row first because the client sends a mostly empty record, so merging it
	 * straight in would null the patient and doctor off the existing one. The
	 * original patient, doctor and created date all stay put.
	 */
	public Response updateDiagnosis(Request request) {

		MedicalRecord changes = (MedicalRecord) request.getParams().get("medicalRecord");

		startSession();

		try {
			MedicalRecord record = session.find(MedicalRecord.class, changes.getRecordId());

			if (record == null) {
				throw new IllegalArgumentException("No medical record with ID " + changes.getRecordId());
			}

			record.setDiagnosis(changes.getDiagnosis());
			record.setTreatmentNote(changes.getTreatmentNote());
			record.setFollowUpDate(changes.getFollowUpDate());

			resp.setSuccess(true);
			resp.setMessage("Medical record updated");

			logger.info("Medical record {} updated", record.getRecordId());

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to update medical record");

			logger.error("Could not update medical record", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Every record, for the doctor's diagnosis table. The one below it is the
	 * per-patient version that the patient's own screen uses.
	 */
	public Response getDiagnosisRecords(Request request) {

		startSession();

		try {
			List<MedicalRecord> records = session
					.createQuery("FROM MedicalRecord ORDER BY createdDate DESC", MedicalRecord.class).list();

			resp.setSuccess(true);
			resp.setMessage("Medical records found");
			resp.setData(records);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get medical records");

			logger.error("Could not load the medical record list", e);

		} finally {
			endSession();
		}

		return resp;
	}

	public Response getMedicalRecords(Request request) {

		String patientId = (String) request.getParams().get("patientId");

		startSession();

		try {
			List<MedicalRecord> records = session
					.createQuery("FROM MedicalRecord WHERE patientId = ?1 ORDER BY createdDate DESC",
							MedicalRecord.class)
					.setParameter(1, patientId).list();

			resp.setSuccess(true);
			resp.setMessage("Medical records found");
			resp.setData(records);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get medical records");

			logger.error("Could not load medical records for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Every reading is its own row and nothing gets overwritten, because the trend
	 * over a shift is the whole point of keeping vitals.
	 */
	public Response recordVitals(Request request) {

		VitalSigns vitals = (VitalSigns) request.getParams().get("vitalSigns");
		String patientId = (String) request.getParams().get("patientId");
		String nurseId = (String) request.getParams().get("nurseId");

		startSession();

		try {

			String normalisedId = patientId == null ? "" : patientId.trim().toUpperCase();

			if (session.find(Patient.class, normalisedId) == null) {
				throw new IllegalArgumentException("No patient with ID " + patientId);
			}

			/*
			 * Yes the client checks these too, but we check again here because anything
			 * else talking to the socket could skip the client entirely. Same limits as
			 * the chk_vitals_temp and chk_vitals_hr constraints, just caught early so the
			 * nurse gets told about the reading instead of seeing a database error.
			 */
			if (vitals.getTemperature() != null && vitals.getTemperature() <= 0) {
				throw new IllegalArgumentException("Temperature must be above zero");
			}

			if (vitals.getHeartRate() != null && vitals.getHeartRate() < 0) {
				throw new IllegalArgumentException("Heart rate can't be negative");
			}

			if (vitals.getRespiratoryRate() != null && vitals.getRespiratoryRate() < 0) {
				throw new IllegalArgumentException("Respiratory rate can't be negative");
			}

			vitals.setPatientId(normalisedId);
			vitals.setNurseId(nurseId);

			session.persist(vitals);

			resp.setSuccess(true);
			resp.setMessage("Vital signs recorded");

			logger.info("Vitals recorded for patient {} by nurse {}", normalisedId, nurseId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to record vitals: " + e.getMessage());

			logger.error("Could not record vitals", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * A nurse's case list is just the readings they took, newest first. The vitals
	 * table on screen reads vitals fields, which is why this sends back VitalSigns
	 * and not complaints.
	 */
	public Response getAssignedCases(Request request) {

		String nurseId = (String) request.getParams().get("nurseId");

		startSession();

		try {
			List<VitalSigns> cases = session
					.createQuery("FROM VitalSigns WHERE nurseId = ?1 ORDER BY recordedAt DESC", VitalSigns.class)
					.setParameter(1, nurseId).list();

			resp.setSuccess(true);
			resp.setMessage("Assigned cases found");
			resp.setData(cases);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get assigned cases");

			logger.error("Could not load cases for nurse {}", nurseId, e);

		} finally {
			endSession();
		}

		return resp;
	}
}
