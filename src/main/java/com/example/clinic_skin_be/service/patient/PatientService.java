// File: com.example.clinic_skin_be.service.patient.PatientService.java

package com.example.clinic_skin_be.service.patient;

import com.example.clinic_skin_be.dto.patient.AppointmentHistorySummaryDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AppointmentHistorySummaryDTO> getAppointmentHistoryForPatient(Long accountId) {

        Query query = entityManager.createNativeQuery(
                "{CALL GetPatientAppointmentHistoryByAccount(:p_account_id)}"
        );
        query.setParameter("p_account_id", accountId);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> {
                    // 0: appointment_id
                    Long appointmentId = row[0] != null ? ((Number) row[0]).longValue() : null;

                    // 1: status
                    String status = (String) row[1];

                    // 2: appointment_date_time
                    String appointmentDateTime = (String) row[2];

                    // 3: appointment_note
                    String appointmentNote = (String) row[3];

                    // 4: record_id
                    Long recordId = row[4] != null ? ((Number) row[4]).longValue() : null;

                    // 5: doctor_name
                    String doctorName = (String) row[5];

                    return new AppointmentHistorySummaryDTO(
                            appointmentId,
                            recordId,
                            status,
                            appointmentDateTime,
                            appointmentNote,
                            doctorName
                    );
                })
                // Xóa dòng NULL từ procedure (WHERE 1=0)
                .filter(dto -> dto.getAppointmentId() != null)
                .collect(Collectors.toList());
    }
}
