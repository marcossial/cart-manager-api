package com.marcossial.cartmanager.controller;

import com.marcossial.cartmanager.domain.Carrinho;
import com.marcossial.cartmanager.dto.CarrinhoRequestDTO;
import com.marcossial.cartmanager.service.CarrinhoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoController {
    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @PostMapping
    public ResponseEntity<Carrinho> registrarCarrinho(@RequestBody CarrinhoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinhoService.registrarCarrinho(dto));
    }

    @GetMapping
    public ResponseEntity<List<Carrinho>> listarCarrinhos() {
        return ResponseEntity.ok(carrinhoService.listarTodosCarrinhos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrinho> encontrarCarrinho(@PathVariable Integer id) {
        return carrinhoService.encontrarCarrinhoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrinho> atualizarCarrinho(@PathVariable Integer id, @RequestBody CarrinhoRequestDTO dto) {
        try {
            return ResponseEntity.ok(carrinhoService.atualizarCarrinhoPorId(id, dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCarrinho(@PathVariable Integer id) {
        carrinhoService.excluirCarrinhoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
