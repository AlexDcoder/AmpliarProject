// AppointmentCreateDTO.java
package com.example.ampliar.dto.appointment;

import java.time.LocalDateTime;
import java.util.List;
import com.example.ampliar.validation.constraints.AppointmentDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record AppointmentCreateDTO(
        @AppointmentDate
        LocalDateTime appointmentDate,

        @NotNull(message = "O psicólogo é obrigatório.")
        Long psychologistId,

        // aceita "patientIds" (novo) e "patientId" (legado, único)
        @NotNull(message = "Informe pelo menos um paciente.")
        @JsonAlias({"patientId"})
        List<Long> patientIds,

        @NotNull(message = "O pagamento é obrigatório.")
        Long paymentId
) {}
