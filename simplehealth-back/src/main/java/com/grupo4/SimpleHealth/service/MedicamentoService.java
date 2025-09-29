package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Medicamento;
import com.grupo4.SimpleHealth.repository.MedicamentoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

  private final MedicamentoRepository medicamentoRepository;

  public List<Medicamento> listar() {
    return medicamentoRepository.findAll();
  }

  public Optional<Medicamento> buscarPorId(Long id) {
    return medicamentoRepository.findById(id);
  }

  public Medicamento salvar(Medicamento medicamento) {
    return medicamentoRepository.save(medicamento);
  }

  public Medicamento atualizar(Long id, Medicamento medicamento) {
    return medicamentoRepository.findById(id)
        .map(m -> {
          medicamento.setIdItem(id);
          return medicamentoRepository.save(medicamento);
        })
        .orElseThrow(() -> new RuntimeException("Medicamento não encontrado"));
  }

  public void deletar(Long id) {
    medicamentoRepository.deleteById(id);
  }
}
