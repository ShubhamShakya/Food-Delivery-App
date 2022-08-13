package com.dataflair.fooddeliveryapp.restaurantadmin;

import com.dataflair.fooddeliveryapp.FDBottomSheet;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.DataAddedSuccessfullyBinding;

public class DataAddedSuccessfully extends FDBottomSheet<DataAddedSuccessfullyBinding> {

    public DataAddedSuccessfully(){

    }

    @Override
    protected int getLayoutId() {
        return R.layout.data_added_successfully;
    }

    @Override
    protected void setupBindingVM() {
        binding = getBinding();
    }

    @Override
    protected void setupUI() {



    }

    @Override
    protected boolean showFullScreen() {
        return false;
    }
}
