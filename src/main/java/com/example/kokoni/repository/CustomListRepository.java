package com.example.kokoni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kokoni.entity.CustomList;
import java.util.List;

@Repository
public interface CustomListRepository extends JpaRepository <CustomList, Long>{
    List<CustomList> findByOwnerId(Long userId);
}
