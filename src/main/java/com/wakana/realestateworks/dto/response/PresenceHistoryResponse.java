package com.wakana.realestateworks.dto.response;

import com.wakana.realestateworks.model.PresenceLog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PresenceHistoryResponse {
    private List<PresenceLog> logs;
    private String totalWorkedTime; // Exemple: "7h 30min"
}
