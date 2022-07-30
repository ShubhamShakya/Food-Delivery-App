package com.dataflair.fooddeliveryapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.dataflair.fooddeliveryapp.databinding.BaseFragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public abstract class FDBottomSheet<T extends ViewDataBinding> extends BottomSheetDialogFragment {
    //private static final String TAG = JLBaseActivity.class.getSimpleName();
    protected T binding;
    BaseFragmentBottomSheetBinding baseFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        baseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.base_fragment_bottom_sheet, container, false);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        baseFragmentBinding.cnstrntContainer.addView(binding.getRoot(), ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        return baseFragmentBinding.getRoot();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (showFullScreen())
            dialog.setOnShowListener(dialogInterface -> setupFullHeight((BottomSheetDialog) getDialog()));
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setupBindingVM();
        setupUI();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(Objects.requireNonNull(bottomSheet));
        behavior.setHideable(false);
        behavior.from(bottomSheet).setPeekHeight(500);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        Integer windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private Integer getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    abstract protected int getLayoutId();

    abstract protected void setupBindingVM();

    abstract protected void setupUI();

    abstract protected boolean showFullScreen();

    public T getBinding() {
        return binding;
    }


}
