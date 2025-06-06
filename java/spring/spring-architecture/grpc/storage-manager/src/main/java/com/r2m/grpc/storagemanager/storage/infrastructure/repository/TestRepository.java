package com.r2m.grpc.storagemanager.storage.infrastructure.repository;

import com.r2m.grpc.storagemanager.storage.domain.JustForTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<JustForTest, String> {
}
