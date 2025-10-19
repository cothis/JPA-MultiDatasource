package jpa.demo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedSaveResponse {
    private Long primaryId;
    private Long secondaryId;
    private String message;
}
