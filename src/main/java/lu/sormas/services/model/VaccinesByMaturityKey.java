package lu.sormas.services.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public class VaccinesByMaturityKey {

    private final Integer maturityKey;

    public VaccinesByMaturityKey(Integer maturityKey) {
        this.maturityKey = maturityKey;
    }

    public String getMaturityName() {
        return String.format("%d months", maturityKey);
    }

    public static VaccinesByMaturityKey month(Integer maturityKey) {
        return new VaccinesByMaturityKey(maturityKey);
    }
}
