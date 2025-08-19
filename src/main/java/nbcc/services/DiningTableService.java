package nbcc.services;

import nbcc.entities.DiningTable;
import nbcc.entities.Event;
import nbcc.entities.Layout;
import nbcc.repositories.DiningTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DiningTableService {
    private final DiningTableRepository diningTableRepository;

    public DiningTableService(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    public List<DiningTable> findAll() {
        //return diningTableRepository.findAll();
        return diningTableRepository.findAllActiveTables();
    }

    public Optional<DiningTable> findById(Long id) {
        return diningTableRepository.findById(id);
    }

    public DiningTable save(DiningTable diningTable) {
        return diningTableRepository.save(diningTable);
    }

    public void delete(Long id) {
        //diningTableRepository.deleteById(id);
        var tableOpt = findById(id);
        if (tableOpt.isPresent()) {
            var table = tableOpt.get();
            table.setArchived(true);
            save(table);
        }
    }

    //still the same as delete at the top might modify later
    @Transactional
    public void archive(Long id) {
        var tableOpt = findById(id);
        if (tableOpt.isPresent()) {
            var table = tableOpt.get();
            table.setArchived(true);
            save(table);
        }
    }

    public List<DiningTable> findTablesWithReservationsForEvent(Event event) {
        return diningTableRepository.findTablesWithReservationsForEvent(event);
    }

    public List<DiningTable> findAvailableTablesForEvent(Event event) {
        return diningTableRepository.findAvailableTablesForEvent(event);
    }

    public List<DiningTable> findAllArchivedTables() {
        return diningTableRepository.findAllArchivedTables();
    }

    public List<DiningTable> findArchivedTablesByLayout(Layout layout) {
        return diningTableRepository.findArchivedTablesByLayout(layout);
    }
} 