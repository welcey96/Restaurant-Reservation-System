package nbcc.controllers;

import jakarta.validation.Valid;
import nbcc.entities.DiningTable;
import nbcc.entities.Layout;
import nbcc.repositories.DiningTableRepository;
import nbcc.services.DiningTableService;
import nbcc.services.LayoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/layout/{layoutId}/diningtable")
public class DiningTableController {

    //access dinning repo
    private final DiningTableRepository diningTableRepository;

    private final LayoutService layoutService;
    private final DiningTableService diningTableService;

    public DiningTableController(DiningTableRepository diningTableRepository, LayoutService layoutService,
                                 DiningTableService diningTableService) {
        this.diningTableRepository = diningTableRepository;
        this.layoutService = layoutService;
        this.diningTableService = diningTableService;
    }

    //this is coming from the url of the layout controller
    @ModelAttribute("layouts")
    public List<Layout> getAllLayouts() {
        return layoutService.findAll();
    }


    //adding to dining
    @GetMapping("/create")
    public String create(@PathVariable Long layoutId, Model model) {
        var layout = layoutService.findById(layoutId);
        if (layout.isPresent()) {
            model.addAttribute("diningTable", new DiningTable());
            model.addAttribute("layout", layout.get());
            return "diningTable/create";
        }
        return "redirect:/layout/edit/";
    }

    @PostMapping("/create")
    public String create(@PathVariable Long layoutId, @Valid DiningTable diningTable, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("layout", layoutService.findById(layoutId).orElse(null));
            return "diningTable/create";
        }

        var layoutOptional = layoutService.findById(layoutId);
        if (layoutOptional.isPresent()) {
            var layout = layoutOptional.get();

            // Change this line to use the helper method
            //this is the helper method from the dinner table
            layout.addDiningTable(diningTable);  // This handles both sides of the relationship

            diningTableRepository.save(diningTable);

            // Update layout last updated timestamp
            layout.setLastUpdated(LocalDateTime.now());
            layoutService.save(layout);
        }

        return "redirect:/layout/edit/" + layoutId;
    }

    @GetMapping("/remove/{tableId}")
    public String delete(@PathVariable Long layoutId, @PathVariable Long tableId) {
        var layoutOptional = layoutService.findById(layoutId);
        var tableOptional = diningTableRepository.findById(tableId);

        if (layoutOptional.isPresent() && tableOptional.isPresent()) {
            var layout = layoutOptional.get();
            var table = tableOptional.get();

            if (table.getLayout() != null && table.getLayout().getId().equals(layoutId)) {

                if (table.getReservationRequest() != null) {
                    // If table has reservations, archive it
                    table.setArchived(true);
                    diningTableRepository.save(table);
                } else {
                    // If no reservations, permanently delete it
                    layout.removeDiningTable(table);
                    diningTableRepository.delete(table);
                }

                layout.setLastUpdated(LocalDateTime.now());
                layoutService.save(layout);
            }
        }

        return "redirect:/layout/edit/" + layoutId;
    }

    @GetMapping("/edit/{tableId}")
    public String edit(@PathVariable Long layoutId, @PathVariable Long tableId, Model model) {
        var layoutOptional = layoutService.findById(layoutId);
        var tableOptional = diningTableRepository.findById(tableId);

        if (layoutOptional.isPresent() && tableOptional.isPresent()) {
            model.addAttribute("diningTable", tableOptional.get());
            model.addAttribute("layout", layoutOptional.get());
            return "diningTable/edit";
        }

        return "redirect:/layout/edit/" + layoutId;
    }

    @PostMapping("/edit/{tableId}")
    public String edit(@PathVariable Long layoutId, @PathVariable Long tableId,
                       @Valid DiningTable diningTable, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("layout", layoutService.findById(layoutId).orElse(null));
            return "diningTable/edit";
        }

        var layoutOptional = layoutService.findById(layoutId);
        var tableOptional = diningTableRepository.findById(tableId);

        if (layoutOptional.isPresent() && tableOptional.isPresent()) {
            var layout = layoutOptional.get();
            var existingTable = tableOptional.get();

            // Update table properties
            existingTable.setNumberOfSeats(diningTable.getNumberOfSeats());
            diningTableRepository.save(existingTable);
            layout.setLastUpdated(LocalDateTime.now());
            layoutService.save(layout);
        }

        return "redirect:/layout/edit/" + layoutId;
    }


}
