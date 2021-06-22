package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class RegisterTandemClaimsRequest {
    private List<KissTandemAdminClaim> adminClaims;
    private String contractAddress;
    private String secretKey;
    private List<KissTandemUserClaim> userClaims;

    /**
     * Gets the value of the adminClaims property.
     *
     * @return possible object is {@link List< KissTandemAdminClaim>}
     */
    public List<KissTandemAdminClaim> getAdminClaims() {
        return adminClaims;
    }

    /**
     * Sets the value of the adminClaims property
     *
     * @param adminClaims allowed object is {@link List< KissTandemAdminClaim> }
     * @return the {@link RegisterTandemClaimsRequest}
     */
    public RegisterTandemClaimsRequest setAdminClaims(List<KissTandemAdminClaim> adminClaims) {
        this.adminClaims = adminClaims;
        return this;
    }

    /**
     * Gets the value of the contractAddress property.
     *
     * @return possible object is {@link String}
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Sets the value of the contractAddress property
     *
     * @param contractAddress allowed object is {@link String }
     * @return the {@link RegisterTandemClaimsRequest}
     */
    public RegisterTandemClaimsRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the secretKey property.
     *
     * @return possible object is {@link String}
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the value of the secretKey property
     *
     * @param secretKey allowed object is {@link String }
     * @return the {@link RegisterTandemClaimsRequest}
     */
    public RegisterTandemClaimsRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * Gets the value of the userClaims property.
     *
     * @return possible object is {@link List< KissTandemUserClaim>}
     */
    public List<KissTandemUserClaim> getUserClaims() {
        return userClaims;
    }

    /**
     * Sets the value of the userClaims property
     *
     * @param userClaims allowed object is {@link List< KissTandemUserClaim> }
     * @return the {@link RegisterTandemClaimsRequest}
     */
    public RegisterTandemClaimsRequest setUserClaims(List<KissTandemUserClaim> userClaims) {
        this.userClaims = userClaims;
        return this;
    }
}
