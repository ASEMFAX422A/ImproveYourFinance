package org.pdf.finanzverwaltung.dto;

public class SettingsDto {

    public int passwordMinLength;
    public int passwordMinNumbers;
    public int passwordMinSpecialCharacters;

    public int usernameMinLength;

    public SettingsDto(int passwordMinLength, int passwordMinNumbers, int passwordMinSpecialCharacters,
            int usernameMinLength) {
        this.passwordMinLength = passwordMinLength;
        this.passwordMinNumbers = passwordMinNumbers;
        this.passwordMinSpecialCharacters = passwordMinSpecialCharacters;

        this.usernameMinLength = usernameMinLength;
    }
}
