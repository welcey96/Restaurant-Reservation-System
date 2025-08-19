package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.Layout;
import nbcc.repositories.DiningTableRepository;
import nbcc.repositories.LayoutRepository;
import nbcc.services.LayoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
//@RequestMapping({"/layout"})
public class LayoutController {

    private final LayoutService layoutService;
    private final LayoutRepository layoutRepository;
    private final DiningTableRepository diningTableRepository;

    public LayoutController(LayoutService layoutService,LayoutRepository layoutRepository, DiningTableRepository diningTableRepository) {
        this.layoutService = layoutService;
        this.layoutRepository = layoutRepository;
        this.diningTableRepository = diningTableRepository;
    }


    @GetMapping("/layout")
    public String getAll(Model model) {
        var values = layoutRepository.findAll();
        model.addAttribute("layouts", values);
        return "layouts/index";
    }

    //create layout
    @GetMapping("/layout/create")
    public String create(Model model) {
        model.addAttribute("layout", new Layout());
        return "layouts/create";
    }

    @PostMapping("/layout/create")
    public String create(@Valid Layout layout, BindingResult result, Model model) {

        if(result.hasErrors()) {
            return "layouts/create";
        }
        layoutRepository.save(layout);
        return "redirect:/layout";
    }

    @GetMapping("/layout/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        var entity = layoutRepository.findById(id);

        if(entity.isPresent()) {
            model.addAttribute("layout", entity.get());
            model.addAttribute("diningTables", entity.get().getDiningTables());
            return "layouts/edit";
        }

        return "redirect:/layout";
    }

    @PostMapping("/layout/edit")
    public String edit(@Valid Layout layout, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "layouts/edit";
        }
        // Retrieve the existing layout from the database
        var existingLayoutOptional = layoutRepository.findById(layout.getId());

        if (existingLayoutOptional.isPresent()) {
            var existingLayout = existingLayoutOptional.get();
            existingLayout.setName(layout.getName());
            existingLayout.setDescription(layout.getDescription());
            existingLayout.setLastUpdated(LocalDateTime.now());
            layoutRepository.save(existingLayout);
        }
        return "redirect:/layout";
    }

    @GetMapping("/layout/detail/{id}")
    public String details(@PathVariable Long id, Model model) {
        var entity = layoutRepository.findById(id);
        if(entity.isPresent()) {
            var layout = entity.get();
            model.addAttribute("layout", entity.get());
            model.addAttribute("diningTables", layout.getDiningTables());
            return "/layouts/detail";
        }
        return "redirect:/layout";
    }

    @GetMapping("/layout/delete/{id}")
    public String deleteConfirmation(@PathVariable Long id, Model model) {
        var entity = layoutRepository.findById(id);
        if(entity.isPresent()) {
            var layout = entity.get();
            model.addAttribute("layout", layout);
            model.addAttribute("diningTables", layout.getDiningTables());
            return "layouts/delete";
        }
        return "redirect:/layout";
    }

    @PostMapping("/layout/delete/{id}")
    public String delete(@PathVariable Long id) {
        layoutService.deleteLayout(id);
        return "redirect:/layout";
    }
}





