package services.impl;

import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.errors.OrderError;
import services.MenuItemService;

import java.util.List;

public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemDAO dao;

    @Inject
    public MenuItemServiceImpl(MenuItemDAO dao) {
        this.dao = dao;
    }


    public Either<OrderError, List<MenuItem>> getAll() {
        return dao.getAll();
    }

    public MenuItem getByName(String name) {
        MenuItem menuItem2 = null;
        List<MenuItem> getAll = dao.getAll().get();
        for (MenuItem menuItem : getAll) {
            if (menuItem.getName().equals(name)) {
                menuItem2 = menuItem;
            }
        }
        return menuItem2;
    }

}
