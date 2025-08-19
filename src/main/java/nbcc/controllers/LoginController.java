package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.dtos.LoginDto;
import nbcc.entities.UserDetail;
import nbcc.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static nbcc.utils.Utilities.setFieldError;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping({"/login"})
    public String login(Model model) {
        model.addAttribute("login", new LoginDto());
        return "auth/login";
    }

    @PostMapping({"/login"})
    public String login(@ModelAttribute("login") @Valid LoginDto login, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (loginService.login(login))
                return "redirect:/";
            model.addAttribute("message", "Invalid username or password");
        }

        return "auth/login";
    }

    @GetMapping({"/register"})
    public String register(Model model) {
        model.addAttribute("register", new UserDetail());
        return "auth/register";
    }

    @PostMapping({"/register"})
    public String register(@ModelAttribute("register") @Valid UserDetail user,
                           BindingResult bindingResult) {

        if (!user.getUsername().isBlank() && loginService.usernameExists(user.getUsername())) {
            setFieldError("username", "Username already exists", bindingResult);
        }

        if (!bindingResult.hasErrors()) {
            if (loginService.register(user))
                return "redirect:/login";
        }

        return "auth/register";
    }

    @PostMapping({"/logout"})
    public String logout() {
        loginService.logout();
        return "redirect:/";
    }
}
