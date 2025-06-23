package com.example.ceubetjava;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.example.ceubetjava.data.User;
import com.example.ceubetjava.data.UserRepository;

public class RegisterBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputLayout usernameInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private MaterialButton createAccountButton;
    private OnRegistrationCompleteListener registrationCompleteListener;
    private UserRepository userRepository;

    public interface OnRegistrationCompleteListener {
        void onRegistrationComplete(String username);
    }

    public void setOnRegistrationCompleteListener(OnRegistrationCompleteListener listener) {
        this.registrationCompleteListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
        userRepository = new UserRepository(requireContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_register, container, false);
        
        usernameInputLayout = view.findViewById(R.id.usernameInputLayout);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordInputLayout);
        createAccountButton = view.findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(v -> handleRegistration());

        return view;
    }

    private void handleRegistration() {
        String username = usernameInputLayout.getEditText().getText().toString().trim();
        String email = emailInputLayout.getEditText().getText().toString().trim();
        String password = passwordInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInputLayout.getEditText().getText().toString().trim();

        if (validateInput(username, email, password, confirmPassword)) {
            userRepository.registerUser(username, email, password, new UserRepository.OnUserCallback() {
                @Override
                public void onSuccess(User user) {
                    showToast("Conta criada com sucesso! Você ganhou 100 créditos iniciais!");
                    
                    if (registrationCompleteListener != null) {
                        registrationCompleteListener.onRegistrationComplete(username);
                    }
                    
                    dismiss();
                }

                @Override
                public void onError(String error) {
                    showToast(error);
                }
            });
        }
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(username)) {
            usernameInputLayout.setError("Digite seu nome de usuário");
            return false;
        }

        if (username.length() < 3) {
            usernameInputLayout.setError("O nome de usuário deve ter pelo menos 3 caracteres");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Digite seu e-mail");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Digite um e-mail válido");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Digite sua senha");
            return false;
        }

        if (password.length() < 6) {
            passwordInputLayout.setError("A senha deve ter pelo menos 6 caracteres");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("As senhas não coincidem");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        if (getContext() == null) return;
        
        requireActivity().runOnUiThread(() -> {
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            View toastView = toast.getView();
            if (toastView != null) {
                toastView.setElevation(25f);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            }
            toast.show();
        });
    }
} 