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

import com.example.kokoni.dto.request.CustomListRequest;
import com.example.kokoni.dto.response.CustomListDetailsResponse;
import com.example.kokoni.dto.response.CustomListSummaryResponse;
import com.example.kokoni.service.CustomListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class CustomListController {

    private final CustomListService customListService;

    @GetMapping
    public ResponseEntity<List<CustomListSummaryResponse>> getMyLists() {
        List<CustomListSummaryResponse> list = customListService.getMyLists();
        return new ResponseEntity<>(list,HttpStatus.OK); 
    }

    @GetMapping("/{listId}")
    public ResponseEntity<CustomListDetailsResponse> getListDetails(@PathVariable Long listId) {
        CustomListDetailsResponse listDetails = customListService.getListDetails(listId);
        return new ResponseEntity<>(listDetails, HttpStatus.OK); 
    }

    @PostMapping
    public ResponseEntity<CustomListSummaryResponse> createList(@Valid @RequestBody CustomListRequest request) {
        return new ResponseEntity<>(customListService.createList(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        customListService.deleteList(listId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{listId}/items/{externalId}")
    public ResponseEntity<Void> addMangaToList(@PathVariable Long listId, @PathVariable String externalId) {
        customListService.addMangaToList(listId, externalId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{listId}/items/{listItemId}")
    public ResponseEntity<Void> removeMangaFromList(@PathVariable Long listId, @PathVariable Long listItemId) {
        customListService.removeMangaFromList(listId, listItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/items/external/{id}")
    public ResponseEntity<Void> removeFromAllLists(@PathVariable String id) {
        customListService.removeFromAllMyLists(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

