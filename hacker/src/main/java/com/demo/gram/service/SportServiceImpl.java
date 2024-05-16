package com.demo.gram.service;


import com.demo.gram.entity.Sport;
import com.demo.gram.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

  private final SportRepository sportRepository;

  @Autowired
  public SportServiceImpl(SportRepository sportRepository) {
    this.sportRepository = sportRepository;
  }

  @Override
  public List<Sport> findAllSports() {
    return sportRepository.findAll();
  }

  @Override
  public Optional<Sport> findSportById(String id) {
    return sportRepository.findById(id);
  }
}
