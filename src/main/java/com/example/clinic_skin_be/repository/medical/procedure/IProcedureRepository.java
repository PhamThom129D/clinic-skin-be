package com.example.clinic_skin_be.repository.medical.procedure;

import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProcedureRepository extends JpaRepository<Procedure,Long> {
}
