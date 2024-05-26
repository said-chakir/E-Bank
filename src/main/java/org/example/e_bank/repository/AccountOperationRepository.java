package org.example.e_bank.repository;

import org.example.e_bank.entities.AccountOperation;
import org.example.e_bank.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
 List<AccountOperation> findByBankAccountId(String accountId);
 Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId , Pageable pageable);

}
