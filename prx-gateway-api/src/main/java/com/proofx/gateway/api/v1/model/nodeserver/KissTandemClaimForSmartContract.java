package com.proofx.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * KissTandemClaimForSmartContract
 *
 * @author ProofX
 * @since 1.0.0
 */
public class KissTandemClaimForSmartContract {
    private List<String> helpers;
    private List<Integer> activities;
    private Integer minutes;
    private List<String> helpees;

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
     * @return the {@link KissTandemClaimForSmartContract}
     */
    public KissTandemClaimForSmartContract setHelpers(List<String> helpers) {
        this.helpers = helpers;
        return this;
    }

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
     * @return the {@link KissTandemClaimForSmartContract}
     */
    public KissTandemClaimForSmartContract setActivities(List<Integer> activities) {
        this.activities = activities;
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
     * @return the {@link KissTandemClaimForSmartContract}
     */
    public KissTandemClaimForSmartContract setMinutes(Integer minutes) {
        this.minutes = minutes;
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
     * @return the {@link KissTandemClaimForSmartContract}
     */
    public KissTandemClaimForSmartContract setHelpees(List<String> helpees) {
        this.helpees = helpees;
        return this;
    }
}
