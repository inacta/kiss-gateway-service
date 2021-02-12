package com.proofx.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetSuspendedActivitiesResponse {
    private List<Integer> suspendedActivities;

    /**
     * Gets the value of the suspendedActivities property.
     *
     * @return possible object is {@link List< Integer>}
     */
    public List<Integer> getSuspendedActivities() {
        return suspendedActivities;
    }

    /**
     * Sets the value of the suspendedActivities property
     *
     * @param suspendedActivities allowed object is {@link List< Integer> }
     * @return the {@link GetSuspendedActivitiesResponse}
     */
    public GetSuspendedActivitiesResponse setSuspendedActivities(List<Integer> suspendedActivities) {
        this.suspendedActivities = suspendedActivities;
        return this;
    }
}
