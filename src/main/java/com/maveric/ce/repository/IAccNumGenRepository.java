package com.maveric.ce.repository;

import com.maveric.ce.entity.AccountNumGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccNumGenRepository extends JpaRepository<AccountNumGenerator,Long> {

}
