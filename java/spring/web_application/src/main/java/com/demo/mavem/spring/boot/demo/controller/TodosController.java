package com.demo.mavem.spring.boot.demo.controller;

import com.demo.mavem.spring.boot.demo.helpers.Helper;
import com.demo.mavem.spring.boot.demo.model.Todo;
import com.demo.mavem.spring.boot.demo.repository.TodoRepository;
import com.demo.mavem.spring.boot.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class TodosController {
    //@Autowired
    //TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value="/list-todos", method = RequestMethod.GET)
    public String showTodosList(ModelMap  model){

        model.put("todos", todoRepository.findByUser(getUserName(model)));
        //model.put("todos", todoService.retrieveTodos(getUserName(model)));
        return "list-todos";
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap  model){
        model.addAttribute("todo", new Todo(0, getUserName(model),"", new Date(), false));
        return "todo";
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.POST)
    public String addTodo(ModelMap  model, @Valid Todo todo, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "todo";
        }

        todo.setUser(getUserName(model));
        todoRepository.save(todo);

        //todoService.addTodo(getUserName(model), todo.getDesc(), new Date(), false);
        return "redirect:/list-todos";
    }

    @RequestMapping(value="/delete-todo", method = RequestMethod.GET)
    public String deleteTodos(@RequestParam int id){

        todoRepository.deleteById(id);
        //todoService.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @RequestMapping(value="/update-todo", method = RequestMethod.GET)
    public String shodUpdateTodo(@RequestParam int id, ModelMap model){
        todoRepository.findById(id).ifPresent(todoToUpdate->model.put("todo", todoToUpdate));
        //Todo todoToUpdate = todoService.retrieveTodo(id);

        return "todo";
    }

    @RequestMapping(value="/update-todo", method = RequestMethod.POST)
    public String updateTodos(ModelMap  model, @Valid Todo todo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "todo";
        }

        todo.setUser(getUserName(model));
        todoRepository.save(todo);
        //todoService.updateTodo(todo);

        return "redirect:/list-todos";
    }

    private String getUserName(ModelMap model) {
        return Helper.getLoggedinUserName();
    }

}
