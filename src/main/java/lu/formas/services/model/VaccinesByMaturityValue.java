package lu.formas.services.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import lu.formas.repository.model.Vaccine;

import java.util.Comparator;

@EqualsAndHashCode(of = {"vaccine"})
@ToString
@Getter
public class VaccinesByMaturityValue {

    private final Vaccine vaccine;
    private final Integer order;
    private final boolean isPlaceholder;

    public VaccinesByMaturityValue(Vaccine vaccine) {
        this(vaccine, 0, false);
    }

    public VaccinesByMaturityValue(Vaccine vaccine, Integer order, boolean isPlaceholder) {
        this.vaccine = vaccine;
        this.order = order;
        this.isPlaceholder = isPlaceholder;
    }

    public static VaccinesByMaturityValue of(Vaccine vaccine) {
        return new VaccinesByMaturityValue(vaccine);
    }

    public static VaccinesByMaturityValue placeholder(VaccinesByMaturityKey key) {
        return new VaccinesByMaturityValue(new Vaccine() {{
            setId(Integer.valueOf(key.hashCode()).longValue());
            setMaturityMonth(key.getMaturityKey());
            setName(key.getMaturityName());
        }}, -1, true);
    }

    public static Comparator<VaccinesByMaturityValue> comparator() {
        val byOrder = Comparator.<VaccinesByMaturityValue>comparingInt(a -> a.order);
        val byMonth = Comparator.<VaccinesByMaturityValue>comparingInt(a -> a.vaccine.getMaturityMonth());

        return byMonth.thenComparing(byOrder);
    }
}
