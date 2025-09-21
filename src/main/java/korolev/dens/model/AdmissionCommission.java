package korolev.dens.model;

public record AdmissionCommission(
        PersonContacts chairman,
        String address,
        String phoneNumber,
        String email
) {}
