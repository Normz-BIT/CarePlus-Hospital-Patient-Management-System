package com.careplus.server.service;

import com.careplus.common.model.MedicalRecord;
import com.careplus.common.model.VitalSigns;
import com.careplus.common.net.Response;

/*
 * MedicalRecordService
 * Intended home of the clinical write paths: doctor diagnoses and nurse vitals.
 *
 * NOT YET IMPLEMENTED. Both methods are placeholders returning null and neither
 * is routed in ClientHandler, so DiagnosisController and VitalsController on the
 * client currently receive empty responses.
 *
 * Does not extend BaseService yet, so it has no session or transaction handling.
 */
public class MedicalRecordService {

	/*
	 * Clinical records are append only by intent: a diagnosis is part of a patient's
	 * medical history, so corrections should add a superseding record rather than
	 * overwrite an existing one. Preserving that history is also what makes the
	 * doctor's view of prior treatment meaningful.
	 */
	public Response addDiagnosis(MedicalRecord record) {
		return null;
	}

	/*
	 * Vitals are a timestamped observation rather than a mutable current state, so
	 * each reading is a new row. Overwriting the previous one would destroy the
	 * trend a nurse or doctor needs to see.
	 *
	 * Range validation belongs here as well as in the client. VitalsController only
	 * checks that the values parse as numbers, so a mistyped temperature reaches
	 * the database unchallenged today.
	 */
	public Response recordVitals(VitalSigns vitalSigns) {
		return null;
	}
}
