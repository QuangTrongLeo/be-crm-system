package nlu.fit.crm_system.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {
    @Size(max = 50, message = "firstName must be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "lastName must be at most 50 characters")
    private String lastName;

    @Email(message = "email must be a valid email address")
    @Size(max = 100, message = "email must be at most 100 characters")
    private String email;

    @Size(max = 20, message = "phone must be at most 20 characters")
    @Pattern(regexp = "^[+\\d][\\d\\s().-]{6,19}$", message = "Invalid phone number format")
    private String phone;

    @Size(max = 100, message = "company must be at most 100 characters")
    private String company;

    @Pattern(regexp = "^(LEAD|POTENTIAL|ACTIVE|INACTIVE)$", message = "Invalid status value")
    private String status;
}
