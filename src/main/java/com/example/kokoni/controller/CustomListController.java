package com.example.kokoni.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.User;
import com.example.kokoni.service.CustomListService;
import com.example.kokoni.service.MediaService;
import com.example.kokoni.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class CustomListController {

    private final CustomListService customListService;
    private final UserService userService;
    private final MediaService mediaService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<CustomList> create(@PathVariable Long userId, @RequestBody CustomList list) {
        User owner = userService.findById(userId);
        return new ResponseEntity<>(customListService.createList(list, owner), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomList>> getByUserId(@PathVariable Long userId) {
        List<CustomList> list = customListService.getListsByUser(userId);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }



    //A REVISAR
    @PostMapping("/{listId}/media/{mediaId}")
    public ResponseEntity<Void> addItem(@PathVariable Long listId, @PathVariable Long mediaId) {
        Media media = mediaService.findById(mediaId); 
        customListService.addItemToList(listId, media);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @DeleteMapping("/{listId}/items/{listItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long listId, @PathVariable Long listItemId) {
        customListService.removeItemToList(listId, listItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    // DELETE /api/lists/5 -> Borra la lista 5 (y sus items por CascadeType.ALL)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customListService.deleteList(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

