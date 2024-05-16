package com.demo.gram.controller;


import com.demo.gram.entity.Sport;
import com.demo.gram.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sports")
public class SportController {

  private final SportService sportService;

  @Autowired
  public SportController(SportService sportService) {
    this.sportService = sportService;
  }

  // 모든 스포츠 종목 조회
  @GetMapping
  public ResponseEntity<List<Sport>> getAllSports() {
    List<Sport> sports = sportService.findAllSports();
    return new ResponseEntity<>(sports, HttpStatus.OK);
  }

  // 특정 스포츠 종목 상세 정보 조회
  @GetMapping("/{id}")
  public ResponseEntity<Sport> getSportById(@PathVariable String id) {
    Optional<Sport> sport = sportService.findSportById(id);
    if (sport.isPresent()) {
      return new ResponseEntity<>(sport.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
