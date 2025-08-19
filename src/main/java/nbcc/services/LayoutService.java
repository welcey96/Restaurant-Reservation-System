package nbcc.services;

import nbcc.entities.Layout;
import nbcc.repositories.LayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LayoutService {

    private final LayoutRepository layoutRepository;

    public LayoutService(LayoutRepository layoutRepository) {
        this.layoutRepository = layoutRepository;
    }

//    public List<Layout> findAll() {
//        return layoutRepository.findAll();
//    }
    public List<Layout> findAll() {
        return layoutRepository.findByArchivedFalse();
    }

    public Optional<Layout> findById(Long id) {
       return layoutRepository.findById(id);
   }

    public Layout save(Layout layout) {
        return layoutRepository.save(layout);
    }

    //added
    public List<Layout> findAllActiveAndSpecific(Long layoutId) {
        return layoutRepository.findActiveAndSpecificLayout(layoutId);
    }

    public void deleteLayout(Long id) {
        Optional<Layout> layoutOpt = layoutRepository.findById(id);
        if (layoutOpt.isEmpty()) {
            return ;
        }

        Layout layout = layoutOpt.get();
        if (layout.getEvents() != null && !layout.getEvents().isEmpty()) {
            // Layout has events, archive it instead of deleting
            layout.setArchived(true);
            layoutRepository.save(layout);

        } else {
            // Layout has no events, safe to delete
            layoutRepository.delete(layout);

        }
    }

}
