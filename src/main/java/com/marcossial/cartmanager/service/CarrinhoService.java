package com.marcossial.cartmanager.service;

import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.domain.enums.StatusCarrinho;
import com.marcossial.cartmanager.dto.CarrinhoRequestDTO;
import com.marcossial.cartmanager.repository.CarrinhoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    // CREATE
    public Carrinho registrarCarrinho(CarrinhoRequestDTO dto) {
        StatusCarrinho status = StatusCarrinho.DISPONIVEL; // Status padrão

        if (dto.nome() == null) {
            throw new RuntimeException("Nome do carrinho não pode ser nulo");
        }
        if (dto.status() != null) {
            status = dto.status(); // Troca para o status do request
        }

        return carrinhoRepository.save(new Carrinho(null, dto.nome(), status));
    }

    // READ
    public List<Carrinho> listarTodosCarrinhos() {
        return carrinhoRepository.findAll();
    }

    public Optional<Carrinho> encontrarCarrinhoPorId(Integer id) {
        return carrinhoRepository.findById(id);
    }

    public Optional<Carrinho> encontrarCarrinhoPorNome(String carrinhoNome) {
        return carrinhoRepository.findByNome(carrinhoNome);
    }

    // UPDATE
    public Carrinho atualizarCarrinhoPorId(Integer id, CarrinhoRequestDTO dto) {
        return carrinhoRepository.findById(id).map((car) -> {
            if (dto.nome() != null) {
                car.setNome(dto.nome());
            }
            if (dto.status() != null) {
                car.setStatus(dto.status());
            }
            return carrinhoRepository.save(car);
        }).orElseThrow(() -> new EntityNotFoundException("Carrinho de ID: " + id + " não encontrado"));
    }

    // DELETE
    public void excluirCarrinhoPorId(Integer id) {
        carrinhoRepository.deleteById(id);
    }

}
