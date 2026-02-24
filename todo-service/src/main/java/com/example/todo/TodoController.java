package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "http://localhost:4200")
public class TodoController {

    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Todo> all() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody Todo todo) {
        return repository.save(todo);
    }

    @PatchMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo updates) {
        return repository.findById(id)
                .map(todo -> {
                    if (updates.getTitle() != null) todo.setTitle(updates.getTitle());
                    if (updates.getDetails() != null) todo.setDetails(updates.getDetails());
                    if (updates.getCompleted() != null) todo.setCompleted(updates.getCompleted());
                    return repository.save(todo);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        repository.deleteById(id);
    }
}
