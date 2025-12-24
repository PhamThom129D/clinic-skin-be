package com.example.clinic_skin_be.service.medical.treatment_template;

import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.medication.IMedicationRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.IStepTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TreatmentItemService {

    private final ILabTestRepository labTestRepository;
    private final IMedicationRepository medicationRepository;
    private final IProcedureRepository procedureRepository;
    private final IStepTypeRepository stepTypeRepository;


    // List all items by type (medication, lab test, procedure)
    public List<LabTest> listlabTests(){ return labTestRepository.findAll(); }
    public  List<Medication> listMedications(){ return medicationRepository.findAll(); }
    public List<Procedure> listProcedures(){ return procedureRepository.findAll(); }
    public List<StepType> listStepTypes(){ return stepTypeRepository.findAll(); }


    // Get item details by ID and type
    public LabTest getLabTestById(Long id){ return labTestRepository.findById(id).orElse(null);}
    public Medication getMedicationById(Long id){ return medicationRepository.findById(id).orElse(null); }
    public Procedure getProcedureById(Long id){ return procedureRepository.findById(id).orElse(null); }
    public StepType getStepTypeById(Long id){ return stepTypeRepository.findById(id).orElse(null);}

    // Save or update item by type
    public LabTest saveLabTest(LabTest labTest) {
        return labTestRepository.save(labTest);
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Procedure saveProcedure(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    public StepType saveStepType(StepType stepType) {
        return stepTypeRepository.save(stepType);
    }
}
