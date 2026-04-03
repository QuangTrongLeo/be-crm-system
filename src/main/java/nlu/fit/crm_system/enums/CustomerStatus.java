package nlu.fit.crm_system.enums;

import java.util.Locale;
import java.util.Optional;

public enum CustomerStatus {
    LEAD,
    POTENTIAL,
    ACTIVE,
    INACTIVE;

    public boolean matches(String value) {
        return fromString(value).map(status -> status == this).orElse(false);
    }

    public static Optional<CustomerStatus> fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(CustomerStatus.valueOf(value.trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
