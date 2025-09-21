package korolev.dens.model;

public record PersonContacts(
        String firstName,
        String lastName,
        String patronymic,
        String email,
        String phoneNumber,
        String address
) {}
