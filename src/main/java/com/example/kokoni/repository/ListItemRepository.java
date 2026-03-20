package com.example.kokoni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kokoni.entity.ListItem;

public interface ListItemRepository extends JpaRepository<ListItem, Long>{
    
}
