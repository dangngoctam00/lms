package com.example.lmsbackend.domain.classmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassTextbookKey implements Serializable {

    private static final long serialVersionUID = -4405302865512421801L;
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "textbook_id")
    private Long textbookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassTextbookKey that = (ClassTextbookKey) o;
        return Objects.equals(classId, that.classId) && Objects.equals(textbookId, that.textbookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, textbookId);
    }
}
