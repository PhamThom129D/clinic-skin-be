package com.example.clinic_skin_be.service.staff;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.model.staff.cashier.Cashier;
import com.example.clinic_skin_be.model.staff.consultation.Consultant;
import com.example.clinic_skin_be.model.staff.labStaff.LabStaff;
import com.example.clinic_skin_be.model.staff.receptionist.Receptionist;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.repository.staff.*;
import com.example.clinic_skin_be.service.user.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class StaffService {

    private final IConsultantRepository consultantRepo;
    private final ICashierRepository cashierRepo;
    private final IReceptionistRepository receptionistRepo;
    private final ILabStaffRepository labStaffRepo;
    private final AccountService accountService;

    /** ================= CREATE ================= */
    @Transactional
    public Consultant createConsultant(AccountRequest request) throws IOException {
        return createStaff(request, "ROLE_CONSULTANT", account -> Consultant.builder().account(account).build(), consultantRepo::save);
    }

    @Transactional
    public Cashier createCashier(AccountRequest request) throws IOException {
        return createStaff(request, "ROLE_CASHIER", account -> Cashier.builder().account(account).build(), cashierRepo::save);
    }

    @Transactional
    public Receptionist createReceptionist(AccountRequest request) throws IOException {
        return createStaff(request, "ROLE_RECEPTIONIST", account -> Receptionist.builder().account(account).build(), receptionistRepo::save);
    }

    @Transactional
    public LabStaff createLabStaff(AccountRequest request) throws IOException {
        return createStaff(request, "ROLE_LAB_STAFF", account -> LabStaff.builder().account(account).build(), labStaffRepo::save);
    }

    // Generic method tái sử dụng
    private <T> T createStaff(AccountRequest request,
                              String roleName,
                              Function<Account, T> staffMapper,
                              Function<T, T> saveFunc) throws IOException {
        // Gán role cho account
        request.setRole(roleName);

        // Tạo account mới thông qua AccountService
        AccountResponse accountResp = accountService.createAccount(request);

        // Lấy entity Account đầy đủ từ DB (không chỉ id)
        Account account = accountService.getAccountEntityById(accountResp.getId());

        // Tạo staff tương ứng
        T staff = staffMapper.apply(account);

        // Lưu staff vào repository
        return saveFunc.apply(staff);
    }


    /** ================= READ ================= */
    public List<Consultant> getAllConsultants() {
        return consultantRepo.findAll();
    }

    public List<Cashier> getAllCashiers() {
        return cashierRepo.findAll();
    }

    public List<Receptionist> getAllReceptionists() {
        return receptionistRepo.findAll();
    }

    public List<LabStaff> getAllLabStaffs() {
        return labStaffRepo.findAll();
    }

    public Consultant getConsultantById(Long id) {
        return consultantRepo.findById(id).orElse(null);
    }

    public Cashier getCashierById(Long id) {
        return cashierRepo.findById(id).orElse(null);
    }

    public Receptionist getReceptionistById(Long id) {
        return receptionistRepo.findById(id).orElse(null);
    }

    public LabStaff getLabStaffById(Long id) {
        return labStaffRepo.findById(id).orElse(null);
    }

    /** ================= UPDATE ================= */
    @Transactional
    public Consultant updateConsultant(Long id, AccountRequest request) throws IOException {
        return updateStaff(id, request, consultantRepo, Consultant::getAccount, Consultant::setAccount);
    }

    @Transactional
    public Cashier updateCashier(Long id, AccountRequest request) throws IOException {
        return updateStaff(id, request, cashierRepo, Cashier::getAccount, Cashier::setAccount);
    }

    @Transactional
    public Receptionist updateReceptionist(Long id, AccountRequest request) throws IOException {
        return updateStaff(id, request, receptionistRepo, Receptionist::getAccount, Receptionist::setAccount);
    }

    @Transactional
    public LabStaff updateLabStaff(Long id, AccountRequest request) throws IOException {
        return updateStaff(id, request, labStaffRepo, LabStaff::getAccount, LabStaff::setAccount);
    }

    private <T> T updateStaff(Long id,
                              AccountRequest request,
                              Object repo,
                              Function<T, Account> getAccountFunc,
                              BiConsumer<T, Account> setAccountFunc) throws IOException {

        T staff = ((JpaRepository<T, Long>) repo).findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Lấy account hiện tại
        Account account = getAccountFunc.apply(staff);

        // Cập nhật account thông qua AccountService
        AccountResponse updatedAccountResp = accountService.updateAccount(account.getId(), request);

        // Lấy entity Account đầy đủ từ DB
        Account updatedAccount = accountService.getAccountEntityById(updatedAccountResp.getId());

        // Gán account đã cập nhật cho staff
        setAccountFunc.accept(staff, updatedAccount);

        return ((JpaRepository<T, Long>) repo).save(staff);
    }


    /** ================= DELETE ================= */
    @Transactional
    public void deleteConsultant(Long id) {
        consultantRepo.deleteById(id);
    }

    @Transactional
    public void deleteCashier(Long id) {
        cashierRepo.deleteById(id);
    }

    @Transactional
    public void deleteReceptionist(Long id) {
        receptionistRepo.deleteById(id);
    }

    @Transactional
    public void deleteLabStaff(Long id) {
        labStaffRepo.deleteById(id);
    }

}
