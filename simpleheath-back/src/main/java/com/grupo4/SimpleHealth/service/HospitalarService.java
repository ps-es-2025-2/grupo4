package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Hospitalar;
import com.grupo4.SimpleHealth.repository.HospitalarRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalarService {

  private final HospitalarRepository hospitalarRepository;

  public List<Hospitalar> listar() {
    return hospitalarRepository.findAll();
  }

  public Optional<Hospitalar> buscarPorId(Long id) {
    return hospitalarRepository.findById(id);
  }

  public Hospitalar salvar(Hospitalar hospitalar) {
    return hospitalarRepository.save(hospitalar);
  }

  public Hospitalar atualizar(Long id, Hospitalar hospitalar) {
    return hospitalarRepository.findById(id)
        .map(h -> {
          hospitalar.setIdItem(id);
          return hospitalarRepository.save(hospitalar);
        })
        .orElseThrow(() -> new RuntimeException("Hospitalar não encontrado"));
  }

  public void deletar(Long id) {
    hospitalarRepository.deleteById(id);
  }
}
