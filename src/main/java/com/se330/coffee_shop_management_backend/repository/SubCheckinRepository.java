package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.SubCheckin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubCheckinRepository extends JpaRepository<SubCheckin, UUID>, JpaSpecificationExecutor<SubCheckin> {

    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    Page<SubCheckin> findAllByShift_Id(UUID shiftId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    Page<SubCheckin> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "shift"})
    List<SubCheckin> findAll();

    @Override
    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    SubCheckin save(SubCheckin subCheckin);
}
