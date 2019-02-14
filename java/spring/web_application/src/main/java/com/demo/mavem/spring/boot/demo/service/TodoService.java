package com.demo.mavem.spring.boot.demo.service;

import com.demo.mavem.spring.boot.demo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();
    private static int todoCount = 3;

    static {
        todos.add(new Todo(1, "user", "Item 1", new Date(), false));
        todos.add(new Todo(2, "user", "Item 2", new Date(), false));
        todos.add(new Todo(3, "user", "Item 3", new Date(), false));
    }

    public List<Todo> retrieveTodos(String user) {
        return todos.stream().filter(todos -> todos.getUser().equals(user)).collect(Collectors.toList());
    }

    public void addTodo(String name, String desc, Date targetDate, boolean isDone) {
        todos.add(new Todo(++todoCount, name, desc, targetDate, isDone));
    }

    public Todo retrieveTodo(int id) {
        return todos.stream()
                    .filter(todo -> todo.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

    public void updateTodo(Todo todo){
        Todo todoToChange = retrieveTodo(todo.getId());

        if(todoToChange != null) {
            todoToChange.setDesc(todo.getDesc());
            todoToChange.setTargetDate(todo.getTargetDate());
        }
    }



    public void deleteTodo(int id) {
        todos.removeIf(item->item.getId() == id);
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }
}
