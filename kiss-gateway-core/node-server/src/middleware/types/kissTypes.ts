import BigNumber from 'bignumber.js';

export interface GetAdminResponse {
  admin: string;
}

export interface GetAllowedActivitiesResponse {
  allowedActivities: number[];
}

export interface GetSuspendedActivitiesResponse {
  suspendedActivities: number[];
}

export interface GetActivityLogAddressResponse {
  activityLogAddress: string;
}

export interface GetNonceResponse {
  nonce: number;
}

export interface CallAddAllowedActivityRequest {
  contractAddress: string;
  newActivity: number;
  secretKey: string;
}

export interface CallSuspendAllowedActivityRequest {
  contractAddress: string;
  activity: number;
  secretKey: string;
}

export interface ChangeActivityLogRequest {
  contractAddress: string;
  newActivityLogAddress: string;
  secretKey: string;
}

export interface ChangeAdminThisRequest {
  contractAddress: string;
  newAdmin: string;
  secretKey: string;
}

export interface CalculateUserSignatureRequest {
  activities: number[];
  contractAddress: string;
  helpers: string[];
  minutes: number;
  secretKey: string;
}

export interface CalculateUserSignatureResponse {
  address: string;
  nonce: number;
  publicKey: string;
  signature: string;
}

/* Types for KISS Admin claim registrations */
export interface KissTandemAdminClaim {
  activities: number[];
  helpees: string[];
  helpers: string[];
  minutes: number;
}

// We include nonce here even though this is not sent to the smart contract
// but only used to generate and verify the signature. The nonce is included
// here since these claims must arrive in the smart contract in
// the correct order if more than one claim in a batch comes from the
// same address. If it does, then these claims must be ordered correctly,
// and the nonce field allows us to do that.
export interface UserTandemProof {
  address: string;
  nonce: number;
  publicKey: string;
  signature: string;
}

/* Types for KISS user claim registrations */
export interface KissTandemUserClaim {
  activities: number[];
  helpers: string[];
  minutes: number;
  proofs: UserTandemProof[];
}

// If the request contains *any* admin claims, the secret key *must* be the secret key
// controlling the admin account of the KISS smart contract.
// If the request * only * contains user claims, the secret key can be for any account
// that contains sufficient tezos funds to pay the transaction fee.
export interface RegisterTandemClaimsRequest {
  adminClaims: KissTandemAdminClaim[];
  contractAddress: string;
  secretKey: string;
  userClaims: KissTandemUserClaim[];
}
