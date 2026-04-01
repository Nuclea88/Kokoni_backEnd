package com.example.kokoni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.kokoni.entity.ListItem;

@Repository
public interface ListItemRepository extends JpaRepository<ListItem, Long>{
    boolean existsByListIdAndMediaId(Long listId, Long mediaId);
    
    boolean existsByMediaId(Long mediaId);
}
