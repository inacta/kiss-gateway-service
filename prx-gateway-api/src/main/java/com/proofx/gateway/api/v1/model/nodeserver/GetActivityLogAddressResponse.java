package com.proofx.gateway.api.v1.model.nodeserver;

public class GetActivityLogAddressResponse {
    private String activityLogAddress;

    /**
     * Gets the value of the activityLogAddress property.
     *
     * @return possible object is {@link String}
     */
    public String getActivityLogAddress() {
        return activityLogAddress;
    }

    /**
     * Sets the value of the activityLogAddress property
     *
     * @param activityLogAddress allowed object is {@link String }
     * @return the {@link GetActivityLogAddressResponse}
     */
    public GetActivityLogAddressResponse setActivityLogAddress(String activityLogAddress) {
        this.activityLogAddress = activityLogAddress;
        return this;
    }
}
