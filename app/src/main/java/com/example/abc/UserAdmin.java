package com.example.abc;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class UserAdmin {

    public String Email;
    public String Password;

    // Use a constructor without parameters if you use getters and/or setters.
    public UserAdmin() {

    }
    // This is another constructor WITH parameters.
    public UserAdmin(String Email, String Password)
    {
        this.Email = Email;
        this.Password = Password;
    }

    public String getUserPassword() {
        return Password;
    }

    public void setUserPassword(String userName) {
        this.Password = Password;
    }

    public String getUserEmail()
    {

        return Email;
    }

    public void setUserEmail(String userEmail) {
        this.Email = Email;
    }
}
