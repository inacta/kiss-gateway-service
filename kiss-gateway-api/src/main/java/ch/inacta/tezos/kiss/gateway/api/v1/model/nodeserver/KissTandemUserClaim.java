package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class KissTandemUserClaim {
    private List<Integer> activities;
    private List<String> helpers;
    private Integer minutes;
    private List<UserTandemProof> proofs;

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
     * @return the {@link KissTandemUserClaim}
     */
    public KissTandemUserClaim setActivities(List<Integer> activities) {
        this.activities = activities;
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
     * @return the {@link KissTandemUserClaim}
     */
    public KissTandemUserClaim setHelpers(List<String> helpers) {
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
     * @return the {@link KissTandemUserClaim}
     */
    public KissTandemUserClaim setMinutes(Integer minutes) {
        this.minutes = minutes;
        return this;
    }

    /**
     * Gets the value of the proofs property.
     *
     * @return possible object is {@link List< UserTandemProof>}
     */
    public List<UserTandemProof> getProofs() {
        return proofs;
    }

    /**
     * Sets the value of the proofs property
     *
     * @param proofs allowed object is {@link List< UserTandemProof> }
     * @return the {@link KissTandemUserClaim}
     */
    public KissTandemUserClaim setProofs(List<UserTandemProof> proofs) {
        this.proofs = proofs;
        return this;
    }
}
