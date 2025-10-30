package com.example.ampliar.mapper;

import com.example.ampliar.dto.appointment.AppointmentDTO;
import com.example.ampliar.model.AppointmentModel;
import org.springframework.stereotype.Service;

import java.util.List;                // <--- IMPORTANTE
import java.util.function.Function;

@Service
public class AppointmentDTOMapper implements Function<AppointmentModel, AppointmentDTO> {
    @Override
    public AppointmentDTO apply(AppointmentModel model) {
        List<Long> patientIds = model.getPatients() == null
                ? List.of()
                : model.getPatients().stream().map(p -> p.getId()).toList();

        return new AppointmentDTO(
                model.getId(),
                model.getAppointmentDate(),
                model.getPsychologist() != null ? model.getPsychologist().getId() : null,
                patientIds,
                model.getPayment() != null ? model.getPayment().getId() : null
        );
    }
}
