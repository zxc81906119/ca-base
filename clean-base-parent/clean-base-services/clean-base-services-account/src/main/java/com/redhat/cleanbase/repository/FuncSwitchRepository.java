package com.redhat.cleanbase.repository;

import com.redhat.cleanbase.repository.model.FuncSwitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncSwitchRepository extends JpaRepository<FuncSwitch, Long> {
    List<FuncSwitch> findByFuncNameAndPathExpressionIsNotNull(String funcName);
}
