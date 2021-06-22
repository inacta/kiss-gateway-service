package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetAllowedActivitiesResponse {
    private List<Integer> allowedActivities;

    /**
     * Gets the value of the allowedActivities property.
     *
     * @return possible object is {@link List< Integer>}
     */
    public List<Integer> getAllowedActivities() {
        return allowedActivities;
    }

    /**
     * Sets the value of the allowedActivities property
     *
     * @param allowedActivities allowed object is {@link List< Integer> }
     * @return the {@link GetAllowedActivitiesResponse}
     */
    public GetAllowedActivitiesResponse setAllowedActivities(List<Integer> allowedActivities) {
        this.allowedActivities = allowedActivities;
        return this;
    }
}
