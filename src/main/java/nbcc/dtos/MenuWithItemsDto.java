package nbcc.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class MenuWithItemsDto extends MenuDto {
    private List<MenuItemDto> items;

    public MenuWithItemsDto(long id, String name, String description, boolean archived,
                            LocalDateTime createdAt, List<MenuItemDto> items) {
        super(id, name, description, archived, createdAt);
        this.items = items;
    }

    public List<MenuItemDto> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDto> items) {
        this.items = items;
    }
}
