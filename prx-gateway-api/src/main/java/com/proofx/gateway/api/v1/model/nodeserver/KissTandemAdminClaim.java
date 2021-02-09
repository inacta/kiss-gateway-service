package com.proofx.gateway.api.v1.model.nodeserver;

import java.util.List;

public class KissTandemAdminClaim {
    private List<Integer> activities;
    private List<String> helpees;
    private List<String> helpers;
    private Integer minutes;

    /**
     * Gets the value of the activities property.
     *
     * @return possible object is {@link List< Integer>}
     */
    public List<Integer> getActivities() {
        return activities;
    }

    /**
     * Sets the value of the activities property
     *
     * @param activities allowed object is {@link List< Integer> }
     * @return the {@link KissTandemAdminClaim}
     */
    public KissTandemAdminClaim setActivities(List<Integer> activities) {
        this.activities = activities;
        return this;
    }

    /**
     * Gets the value of the helpees property.
     *
     * @return possible object is {@link List< String>}
     */
    public List<String> getHelpees() {
        return helpees;
    }

    /**
     * Sets the value of the helpees property
     *
     * @param helpees allowed object is {@link List< String> }
     * @return the {@link KissTandemAdminClaim}
     */
    public KissTandemAdminClaim setHelpees(List<String> helpees) {
        this.helpees = helpees;
        return this;
    }

    /**
     * Gets the value of the helpers property.
     *
     * @return possible object is {@link List< String>}
     */
    public List<String> getHelpers() {
        return helpers;
    }

    /**
     * Sets the value of the helpers property
     *
     * @param helpers allowed object is {@link List< String> }
     * @return the {@link KissTandemAdminClaim}
     */
    public KissTandemAdminClaim setHelpers(List<String> helpers) {
        this.helpers = helpers;
        return this;
    }

    /**
     * Gets the value of the minutes property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getMinutes() {
        return minutes;
    }

    /**
     * Sets the value of the minutes property
     *
     * @param minutes allowed object is {@link Integer }
     * @return the {@link KissTandemAdminClaim}
     */
    public KissTandemAdminClaim setMinutes(Integer minutes) {
        this.minutes = minutes;
        return this;
    }
}
