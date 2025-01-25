package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String REDIRECT_TO_USERS_LIST = "redirect:/users";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "list";
    }

    @GetMapping({"/form", "/link"})
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        User user = id != null ? userService.getUserById(id) : new User();
        model.addAttribute("user", user);
        return "form";
    }

    @GetMapping("/link")
    public String showLink(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);

        model.addAttribute("totalUsers", allUsers.size());

        if (!allUsers.isEmpty()) {
            model.addAttribute("lastUser", allUsers.get(allUsers.size() - 1));
        }

        return "link";
    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute User user) {
        if (user.getId() == null) {
            userService.addUser(user);
        } else {
            userService.updateUser(user);
        }
        return REDIRECT_TO_USERS_LIST;
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return REDIRECT_TO_USERS_LIST;
    }
}