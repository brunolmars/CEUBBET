package com.example.ceubetjava;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.ceubetjava.data.User;
import com.example.ceubetjava.data.UserRepository;
import com.example.ceubetjava.adapter.UsersAdapter;
import java.util.List;

/**
 * Tela de login do CEUBET
 * Permite login, registro e modo convidado
 */
public class LoginActivity extends AppCompatActivity implements RegisterBottomSheetDialog.OnRegistrationCompleteListener {
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private MaterialButton showDatabaseButton;
    private TextView guestLoginText;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences prefs;
    private UserRepository userRepository;
    private boolean isCheckingLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("CeubetPrefs", MODE_PRIVATE);
        userRepository = new UserRepository(this);
        
        initializeViews();
        setupClickListeners();

        
        checkSavedLogin();
    }

    private void checkSavedLogin() {
        if (isCheckingLogin) return;

        if (prefs.getBoolean("isLoggedIn", false) && prefs.getBoolean("rememberMe", false)) {
            String savedUsername = prefs.getString("currentUser", "");
            if (!TextUtils.isEmpty(savedUsername)) {
                isCheckingLogin = true;
                loginButton.setEnabled(false);
                userRepository.getUserByUsername(savedUsername, new UserRepository.OnUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        runOnUiThread(() -> {
                            isCheckingLogin = false;
                            loginButton.setEnabled(true);
                            saveLoginState(savedUsername);
                            showToast("Bem-vindo de volta!");
                            onLoginSuccess(false, savedUsername, user.getId());
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            isCheckingLogin = false;
                            loginButton.setEnabled(true);
                            clearSavedLogin();
                            showToast("Usuário salvo não encontrado. Faça login novamente.");
                        });
                    }
                });
            } else {
                clearSavedLogin();
            }
        }

        String savedUsername = prefs.getString("lastUsername", "");
        if (!TextUtils.isEmpty(savedUsername)) {
            usernameInput.setText(savedUsername);
            rememberMeCheckbox.setChecked(prefs.getBoolean("rememberMe", false));
        }
    }

    private void clearSavedLogin() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("isLoggedIn");
        editor.remove("currentUser");
        editor.remove("lastUsername");
        editor.remove("rememberMe");
        editor.apply();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        showDatabaseButton = findViewById(R.id.showDatabaseButton);
        guestLoginText = findViewById(R.id.guestLoginText);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
        registerButton.setOnClickListener(v -> showRegisterBottomSheet());
        guestLoginText.setOnClickListener(v -> onLoginSuccess(true, null, 0));
        showDatabaseButton.setOnClickListener(v -> showUsersDialog());
    }

    private void showRegisterBottomSheet() {
        RegisterBottomSheetDialog bottomSheet = new RegisterBottomSheetDialog();
        bottomSheet.setOnRegistrationCompleteListener(this);
        bottomSheet.show(getSupportFragmentManager(), "RegisterBottomSheet");
    }

    @Override
    public void onRegistrationComplete(String username) {
        saveLoginState(username);
        onLoginSuccess(false, username, 0);
        finish();
    }

    private void handleLogin() {
        if (isCheckingLogin) return;

        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (validateInput(username, password)) {
            isCheckingLogin = true;
            loginButton.setEnabled(false);
            
            userRepository.login(username, password, new UserRepository.OnUserCallback() {
                @Override
                public void onSuccess(User user) {
                    runOnUiThread(() -> {
                        isCheckingLogin = false;
                        loginButton.setEnabled(true);
                        
                        
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("lastUsername", username);
                        editor.putBoolean("rememberMe", rememberMeCheckbox.isChecked());
                        editor.apply();
                        
                        
                        saveLoginState(username);
                        showToast("Bem-vindo de volta!");
                        onLoginSuccess(false, user.getUsername(), user.getId());
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        isCheckingLogin = false;
                        loginButton.setEnabled(true);
                        showToast("Nome de usuário ou senha incorretos");
                    });
                }
            });
        }
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Digite seu nome de usuário");
            return false;
        }
        if (username.length() < 3) {
            usernameInput.setError("O nome de usuário deve ter pelo menos 3 caracteres");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Digite sua senha");
            return false;
        }
        if (password.length() < 6) {
            passwordInput.setError("A senha deve ter pelo menos 6 caracteres");
            return false;
        }
        return true;
    }

    private void saveLoginState(String username) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("currentUser", username);
        editor.apply();
    }

    private void onLoginSuccess(boolean isGuest, String username, int userId) {
        Intent intent = new Intent(this, GameSelectionActivity.class);
        intent.putExtra("isGuest", isGuest);
        if (!isGuest) {
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
        }
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            View toastView = toast.getView();
            if (toastView != null) {
                toastView.setElevation(25f);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            }
            toast.show();
        });
    }

    private void showUsersDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_admin_users);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView recyclerView = dialog.findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UsersAdapter adapter = new UsersAdapter();
        recyclerView.setAdapter(adapter);

            
        userRepository.getAllUsers(new UserRepository.OnUsersCallback() {
            @Override
            public void onSuccess(List<User> users) {
                runOnUiThread(() -> {
                    adapter.setUsers(users);
                    if (users.isEmpty()) {
                        showToast("Nenhum usuário registrado");
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showToast("Erro ao carregar usuários: " + error));
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (!rememberMeCheckbox.isChecked()) {
            clearSavedLogin();
        }
    }
} 