package se.vgregion.portal.vap.domain.jpa;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Patrik Bergström
 */
@Embeddable
public class FlagPk implements Serializable {

    private Long userId;
    private String documentId;

    public FlagPk() {
    }

    public FlagPk(Long userId, String documentId) {
        this.userId = userId;
        this.documentId = documentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlagPk flagPk = (FlagPk) o;

        if (documentId != null ? !documentId.equals(flagPk.documentId) : flagPk.documentId != null) return false;
        if (userId != null ? !userId.equals(flagPk.userId) : flagPk.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (documentId != null ? documentId.hashCode() : 0);
        return result;
    }
}
