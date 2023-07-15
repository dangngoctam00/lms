package com.example.lmsbackend.domain.compositekey;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UnitTextBookKey implements Serializable {
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "textbook_id")
    private Long textbookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitTextBookKey that = (UnitTextBookKey) o;
        if (unitId == null) {
            if (that.unitId != null)
                return false;
        } else if (!unitId.equals(that.unitId))
            return false;

        if (textbookId == null) {
            if (that.textbookId != null)
                return false;
        } else if (!textbookId.equals(that.textbookId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId, textbookId);
    }
}
