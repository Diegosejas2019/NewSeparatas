package com.example.mislibros.model;
import com.google.gson.annotations.SerializedName;
public class User {

    public User(String userName,String password) {
        UserName = userName;
        Password = password;
    }
    public User() {
    }

    @SerializedName("IdUser")
    public int IdUser;

    @SerializedName("IdUserRedSocial")
    public int IdUserRedSocial;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("FullUserName")
    public String FullUserName;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Telefono")
    public String Telefono;

    @SerializedName("UrlFoto")
    public String UrlFoto;

    @SerializedName("TipoRed")
    public String TipoRed;

    @SerializedName("token")
    public String token;

    public int getIdUser() {
        return IdUser;
    }

    public String getUserName() {
        return UserName;
    }

    public String getFullUserName() {
        return FullUserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setFullUserName(String fullUserName) {
        FullUserName = fullUserName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getUrlFoto() {
        return UrlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        UrlFoto = urlFoto;
    }

    public String getTipoRed() {
        return TipoRed;
    }

    public void setTipoRed(String tipoRed) {
        TipoRed = tipoRed;
    }

    @Override
    public String toString() {
        return "Post{" +
                "email='" + this.Email + '\'' +
                ", password='" + this.Password +
                '}';
    }

    public int getIdUserRedSocial() {
        return IdUserRedSocial;
    }

    public void setIdUserRedSocial(int idUserRedSocial) {
        IdUserRedSocial = idUserRedSocial;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
