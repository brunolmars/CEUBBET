package com.example.ceubetjava.data;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

import com.example.ceubetjava.data.UserDao;
import com.example.ceubetjava.data.AppDatabase;
import com.example.ceubetjava.data.User;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public interface OnUserCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface OnUsersCallback {
        void onSuccess(List<User> users);
        void onError(String error);
    }

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getUserById(int userId, OnUserCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserById(userId);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Usuário não encontrado");
                }
            } catch (Exception e) {
                callback.onError("Erro ao buscar usuário: " + e.getMessage());
            }
        });
    }

    public void login(String username, String password, OnUserCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.login(username, password);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Nome de usuário ou senha inválidos");
                }
            } catch (Exception e) {
                callback.onError("Erro ao fazer login: " + e.getMessage());
            }
        });
    }

    public void registerUser(String username, String email, String password, OnUserCallback callback) {
        executorService.execute(() -> {
            try {
                // Verificar se o nome de usuário já existe
                User existingUser = userDao.getUserByUsername(username);
                if (existingUser != null) {
                    callback.onError("Nome de usuário já está em uso");
                    return;
                }

                // Criar novo usuário
                User user = new User(username, email, password);
                long userId = userDao.insert(user);
                user.setId((int) userId);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError("Erro ao criar conta: " + e.getMessage());
            }
        });
    }

    public void getUserByUsername(String username, OnUserCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByUsername(username);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Usuário não encontrado");
                }
            } catch (Exception e) {
                callback.onError("Erro ao buscar usuário: " + e.getMessage());
            }
        });
    }

    public void getAllUsers(OnUsersCallback callback) {
        executorService.execute(() -> {
            try {
                List<User> users = userDao.getAllUsers();
                callback.onSuccess(users);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
} 