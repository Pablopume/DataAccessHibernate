package model.errors;

import common.Constants;

public final class OrderErrorEmptyList extends OrderError {

    public OrderErrorEmptyList() {
        super(Constants.THE_ORDER_LIST_IS_EMPTY);
    }
}
