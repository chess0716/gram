package com.demo.gram.service;

import com.demo.gram.model.Sport;
import java.util.List;
import java.util.Optional;

public interface SportService {
  List<Sport> findAllSports();
  Optional<Sport> findSportById(String id);
}
