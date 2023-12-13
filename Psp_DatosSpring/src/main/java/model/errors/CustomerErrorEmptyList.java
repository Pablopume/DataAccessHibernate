package model.errors;

import common.Constants;

public final class CustomerErrorEmptyList extends CustomerError {

    public CustomerErrorEmptyList() {
        super(Constants.THE_CUSTOMER_LIST_IS_EMPTY_N, Constants.THE_CUSTOMER_LIST_IS_EMPTY_S);
    }
}
