package dev.luan.digitalbank.repositories;

import dev.luan.digitalbank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
