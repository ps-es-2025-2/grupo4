package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Alimento;
import com.grupo4.SimpleHealth.repository.AlimentoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlimentoService {

  private final AlimentoRepository alimentoRepository;

  public List<Alimento> listar() {
    return alimentoRepository.findAll();
  }

  public Optional<Alimento> buscarPorId(Long id) {
    return alimentoRepository.findById(id);
  }

  public Alimento salvar(Alimento alimento) {
    return alimentoRepository.save(alimento);
  }

  public Alimento atualizar(Long id, Alimento alimento) {
    return alimentoRepository.findById(id)
        .map(a -> {
          alimento.setIdItem(id);
          return alimentoRepository.save(alimento);
        })
        .orElseThrow(() -> new RuntimeException("Alimento não encontrado"));
  }

  public void deletar(Long id) {
    alimentoRepository.deleteById(id);
  }
}
