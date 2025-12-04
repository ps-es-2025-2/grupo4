package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Alimento;
import com.simplehealth.estoque.domain.entity.Hospitalar;
import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.domain.entity.Medicamento;
import com.simplehealth.estoque.domain.enums.TipoItem;
import com.simplehealth.estoque.infrastructure.repositories.AlimentoRepository;
import com.simplehealth.estoque.infrastructure.repositories.HospitalarRepository;
import com.simplehealth.estoque.infrastructure.repositories.MedicamentoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final AlimentoRepository alimentoRepository;
  private final MedicamentoRepository medicamentoRepository;
  private final HospitalarRepository hospitalarRepository;

  public Item salvar(Item item) {
    if (item instanceof Alimento) {
      return alimentoRepository.save((Alimento) item);
    } else if (item instanceof Medicamento) {
      return medicamentoRepository.save((Medicamento) item);
    } else if (item instanceof Hospitalar) {
      return hospitalarRepository.save((Hospitalar) item);
    }
    throw new IllegalArgumentException("Tipo de item desconhecido: " + item.getClass().getSimpleName());
  }

  public Item buscarPorId(UUID id) {
    Optional<Alimento> alimento = alimentoRepository.findById(id);
    if (alimento.isPresent()) {
      return alimento.get();
    }
    Optional<Medicamento> medicamento = medicamentoRepository.findById(id);
    if (medicamento.isPresent()) {
      return medicamento.get();
    }
    Optional<Hospitalar> hospitalar = hospitalarRepository.findById(id);
    if (hospitalar.isPresent()) {
      return hospitalar.get();
    }
    throw new IllegalArgumentException("Item não encontrado.");
  }

  public Item buscarPorId(UUID id, TipoItem tipo) {
    if (tipo == null) {
      return buscarPorId(id);
    }
    switch (tipo) {
      case ALIMENTO:
        return alimentoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Alimento não encontrado."));
      case MEDICAMENTO:
        return medicamentoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Medicamento não encontrado."));
      case HOSPITALAR:
        return hospitalarRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Material Hospitalar não encontrado."));
      default:
        throw new IllegalArgumentException("Tipo de item inválido.");
    }
  }

  public List<Item> listarTodos() {
    List<Item> todos = new ArrayList<>();
    todos.addAll(alimentoRepository.findAll());
    todos.addAll(medicamentoRepository.findAll());
    todos.addAll(hospitalarRepository.findAll());
    return todos;
  }

  public void deletar(UUID id) {
    if (alimentoRepository.existsById(id)) {
      alimentoRepository.deleteById(id);
    } else if (medicamentoRepository.existsById(id)) {
      medicamentoRepository.deleteById(id);
    } else if (hospitalarRepository.existsById(id)) {
      hospitalarRepository.deleteById(id);
    } else {
      throw new IllegalArgumentException("Item não encontrado para deleção.");
    }
  }
}
