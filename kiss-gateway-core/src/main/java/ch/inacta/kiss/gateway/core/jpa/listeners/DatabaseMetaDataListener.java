package ch.inacta.kiss.gateway.core.jpa.listeners;

import ch.inacta.kiss.gateway.core.jpa.BaseEntity;

import javax.enterprise.context.ContextNotActiveException;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Database listener which is used to set base fields automatically.
 *
 * @author inacta AG
 * @since 1.0.0
 */
public class DatabaseMetaDataListener {

    @PrePersist
    private void setMetaData(final Object o) {

        if (o instanceof BaseEntity) {

            if (((BaseEntity) o).getCreated() == null) {
                ((BaseEntity) o).setCreated(LocalDateTime.now());
            }

            ((BaseEntity) o).setLastUpdate(LocalDateTime.now());
        }
    }

    @PreUpdate
    private void handleUpdate(final Object o) {
        try {
            if (o instanceof BaseEntity) {
                ((BaseEntity) o).setLastUpdate(LocalDateTime.now());
            }
            // could happen if there is no CDI-Context e.g. external thread call
        } catch (final ContextNotActiveException ex) {
            ((BaseEntity) o).setLastUpdate(LocalDateTime.now());
        }
    }

}
