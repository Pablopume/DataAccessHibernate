package services;

import io.vavr.control.Either;
import model.MenuItem;
import model.errors.OrderError;

import java.util.List;

public interface MenuItemService {
    Either<OrderError, List<MenuItem>> getAll();
    MenuItem getByName(String name);

}
