package pl.app.account.model.command;

public record UpdateAccountCommand(String pesel, String name, String surname) {
}
