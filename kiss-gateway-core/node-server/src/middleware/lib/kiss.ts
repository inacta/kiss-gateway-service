import { RegisterTandemClaimsRequest, KissTandemAdminClaim, KissTandemUserClaim, ChangeActivityLogRequest, ChangeAdminThisRequest, CallAddAllowedActivityRequest, CallSuspendAllowedActivityRequest, GetAdminResponse, GetAllowedActivitiesResponse, GetSuspendedActivitiesResponse, GetActivityLogAddressResponse, CalculateUserSignatureRequest, CalculateUserSignatureResponse, GetNonceResponse } from '../types/kissTypes';
import { packFourTupleAsLeftBalancedPairs, toHexString } from 'tezos-pack';
import { isContractAddress, rpc } from './shared/tokenImplemenation';
import { TransactionResponse } from '../types/responseTypes';
import { TransferStatus } from '../types/types';
import { validateAddress, ValidationResult } from '@taquito/utils';
import { InMemorySigner } from '@taquito/signer';
import { TezosToolkit, ContractAbstraction, ContractProvider } from '@taquito/taquito';
import { TransactionOperation } from '@taquito/taquito/dist/types/operations/transaction-operation';
import BigNumber from 'bignumber.js';

export async function getAdmin(contractAddress: string): Promise<GetAdminResponse | undefined> {
  // Input validation
  if (!isContractAddress(contractAddress)) {
    console.debug('Invalid contract address received. Got: ' + contractAddress);
    return undefined;
  }

  // Read from  storage
  const client = new TezosToolkit(rpc);
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(contractAddress);
  return contract.storage().then((s: any) => {
    const admin: string = s.admin;
    return {
      admin,
    } as GetAdminResponse;
  });
}

export async function getAllowedActivities(contractAddress: string): Promise<GetAllowedActivitiesResponse | undefined> {
  // Input validation
  if (!isContractAddress(contractAddress)) {
    console.debug('Invalid contract address received. Got: ' + contractAddress);
    return undefined;
  }

  // Read from storage
  const client = new TezosToolkit(rpc);
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(contractAddress);
  const storage: any = await contract.storage();
  const allowedActivitiesStorage: any = storage.allowed_activities;
  let gen = allowedActivitiesStorage.entries();
  let val: [string, boolean] = gen.next().value; // e.g. ('1', false)
  const allowedActivities = [] as number[];
  while (val) {
    if (val[1]) {
      allowedActivities.push(parseInt(val[0]));
    }
    val = gen.next().value;
  }
  return {
    allowedActivities,
  } as GetAllowedActivitiesResponse;
}

export async function getSuspendedActivities(contractAddress: string): Promise<GetSuspendedActivitiesResponse | undefined> {
  // Input validation
  if (!isContractAddress(contractAddress)) {
    console.debug('Invalid contract address received. Got: ' + contractAddress);
    return undefined;
  }

  // Read from storage
  const client = new TezosToolkit(rpc);
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(contractAddress);
  return contract.storage().then((s: any) => {
    const suspendedActivitiesStorage: any = s.allowed_activities;
    let gen = suspendedActivitiesStorage.entries();
    let val: [string, boolean] = gen.next().value; // e.g. ('1', false)
    const suspendedActivities = [] as number[];
    while (val) {
      if (!val[1]) {
        suspendedActivities.push(parseInt(val[0]));
      }
      val = gen.next().value;
    }
    return {
      suspendedActivities,
    } as GetSuspendedActivitiesResponse;
  });
}

export async function getActivityLogAddress(contractAddress: string): Promise<GetActivityLogAddressResponse | undefined> {
  // Input validation
  if (!isContractAddress(contractAddress)) {
    console.debug('Invalid contract address received. Got: ' + contractAddress);
    return undefined;
  }

  // Read from storage
  const client = new TezosToolkit(rpc);
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(contractAddress);
  return contract.storage().then((s: any) => {
    const activityLogAddress: string = s.external_contract_address;
    return {
      activityLogAddress,
    } as GetActivityLogAddressResponse;
  });
}

async function getNonceInternal(contractAddress: string, address: string, client: TezosToolkit): Promise<number> {
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(contractAddress);
  const storage: any = await contract.storage();
  const nonce: string = await storage.nonces.get(address) ?? '0';
  return parseInt(nonce);
}

export async function getNonce(contractAddress: string, address: string): Promise<GetNonceResponse> {
  const client = new TezosToolkit(rpc);
  const nonce: number = await getNonceInternal(contractAddress, address, client);
  return {
    nonce
  };
}

export async function calculateUserSignature(request: CalculateUserSignatureRequest): Promise<CalculateUserSignatureResponse | undefined> {
  // Input validation
  if (request.activities === undefined
    || request.secretKey === undefined
    || request.contractAddress === undefined
    || request.helpers === undefined
    || request.minutes === undefined) {
    return undefined;
  }
  if (!isContractAddress(request.contractAddress)) {
    console.log('Invalid contract address');
    return undefined;
  }
  if (request.helpers.some((helper) => validateAddress(helper) !== ValidationResult.VALID)) {
    console.log('Invalid helper value');
    return undefined;
  }

  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return undefined;
  }

  const sender = await signer.publicKeyHash();
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });

  const nonce: number = await getNonceInternal(request.contractAddress, sender, client);

  let msgBytes: Uint8Array;
  try {
    msgBytes = packFourTupleAsLeftBalancedPairs(
      new BigNumber(nonce),
      new BigNumber(request.minutes),
      request.activities.map((x) => new BigNumber(x)),
      request.helpers);
  } catch (e) {
    console.log(e);
    return;
  }

  const publicKey = await signer.publicKey();
  const address = await signer.publicKeyHash();
  const signature = await signer.sign(toHexString(msgBytes));

  return {
    address,
    nonce,
    publicKey,
    signature: signature.sig
  } as CalculateUserSignatureResponse;
}

export async function callSuspendAllowedActivity(request: CallSuspendAllowedActivityRequest): Promise<TransactionResponse> {
  // Input validation
  if (request.activity === undefined || request.secretKey === undefined || request.contractAddress === undefined) {
    return new TransactionResponse(TransferStatus.ERROR, 'Malformed request');
  }
  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }

  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Construct elements to interact with blockchain
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const sender = await signer.publicKeyHash();

  // Send the transaction
  let tx: TransactionOperation;
  try {
    tx = await contract.methods.suspend_allowed_activity(request.activity).send();
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Respond to caller
  return new TransactionResponse(
    TransferStatus.SUCCESS,
    'transaction sent',
    sender,
    tx.hash,
    (tx.fee / parseFloat(tx.consumedGas)).toString(),
    tx.consumedGas,
    tx.gasLimit.toString()
  );
}

export async function callAddAllowedActivity(request: CallAddAllowedActivityRequest): Promise<TransactionResponse> {
  // Input validation
  if (request.newActivity === undefined || request.secretKey === undefined || request.contractAddress === undefined) {
    return new TransactionResponse(TransferStatus.ERROR, 'Malformed request');
  }
  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }

  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Construct elements to interact with blockchain
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const sender = await signer.publicKeyHash();

  console.log(contract.methods);

  // Send the transaction
  let tx: TransactionOperation;
  try {
    tx = await contract.methods.add_allowed_activity(request.newActivity).send();
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Respond to caller
  return new TransactionResponse(
    TransferStatus.SUCCESS,
    'transaction sent',
    sender,
    tx.hash,
    (tx.fee / parseFloat(tx.consumedGas)).toString(),
    tx.consumedGas,
    tx.gasLimit.toString()
  );
}

/**
 * Warning! If the admin is changed to an address that you do not have the secret key for,
 * this change is irreversible!
 */
export async function changeAdminThis(request: ChangeAdminThisRequest): Promise<TransactionResponse> {
  // Input validation
  if (request.newAdmin === undefined || request.secretKey === undefined || request.contractAddress === undefined) {
    return new TransactionResponse(TransferStatus.ERROR, 'Malformed request');
  }
  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }
  if (validateAddress(request.newAdmin) !== ValidationResult.VALID) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid address for a new admin provided');
  }

  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Construct elements to interact with blockchain
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const sender = await signer.publicKeyHash();

  // Send the transaction
  let tx: TransactionOperation;
  try {
    tx = await contract.methods.change_admin_this(request.newAdmin).send();
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Respond to caller
  return new TransactionResponse(
    TransferStatus.SUCCESS,
    'transaction sent',
    sender,
    tx.hash,
    (tx.fee / parseFloat(tx.consumedGas)).toString(),
    tx.consumedGas,
    tx.gasLimit.toString()
  );
}

export async function changeActivityLog(request: ChangeActivityLogRequest): Promise<TransactionResponse> {
  // Input validation
  if (request.newActivityLogAddress === undefined || request.secretKey === undefined || request.contractAddress === undefined) {
    return new TransactionResponse(TransferStatus.ERROR, 'Malformed request');
  }
  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }
  if (!isContractAddress(request.newActivityLogAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid address for a new activity log provided');
  }

  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Construct elements to interact with blockchain
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const sender = await signer.publicKeyHash();

  // Send the transaction
  let tx: TransactionOperation;
  try {
    tx = await contract.methods.change_activity_log(request.newActivityLogAddress).send();
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Respond to caller
  return new TransactionResponse(
    TransferStatus.SUCCESS,
    'transaction sent',
    sender,
    tx.hash,
    (tx.fee / parseFloat(tx.consumedGas)).toString(),
    tx.consumedGas,
    tx.gasLimit.toString()
  );
}

export async function registerTandemClaims(request: RegisterTandemClaimsRequest): Promise<TransactionResponse> {
  // Helper functions
  function adminClaimRequestObjectToSmartContractObject(adminClaim: KissTandemAdminClaim) {
    return {
      helpers: adminClaim.helpers,
      activities: adminClaim.activities,
      minutes: adminClaim.minutes,
      helpees: {
        admin_helpee: adminClaim.helpees
      }
    };
  }
  function userClaimRequestObjectToSmartContractObject(userClaim: KissTandemUserClaim) {
    return {
      helpers: userClaim.helpers,
      activities: userClaim.activities,
      minutes: userClaim.minutes,
      helpees: {
        signed_helpee: userClaim.proofs.map((proof) => {
          return {
            address: proof.address,
            pk: proof.publicKey,
            signature: proof.signature,
          };
        }),
      }
    };
  }

  // Input validation
  if (request.adminClaims === undefined
    || request.contractAddress === undefined
    || request.secretKey === undefined
    || request.userClaims === undefined) {
      return new TransactionResponse(TransferStatus.ERROR, 'Malformed request');
    }

  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }

  if (request.adminClaims.length === 0 && request.userClaims.length === 0) {
    return new TransactionResponse(TransferStatus.ERROR, 'Empty claim lists received');
  }

  if (request.adminClaims.some((claim) => claim.helpees.some((helpee) => validateAddress(helpee) !== ValidationResult.VALID))) {
    return new TransactionResponse(TransferStatus.ERROR, 'Provided spender address is invalid for admin claim');
  }
  if (request.adminClaims.some((claim) => claim.helpers.some((helper) => validateAddress(helper) !== ValidationResult.VALID))) {
    return new TransactionResponse(TransferStatus.ERROR, 'Provided recipient address is invalid for admin claim');
  }
  if (request.userClaims.some((claim) => claim.helpers.some((helper) => validateAddress(helper) !== ValidationResult.VALID))) {
    return new TransactionResponse(TransferStatus.ERROR, 'Provided recipient address is invalid for user claim');
  }
  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Construct elements to interact with blockchain
  const client = new TezosToolkit(rpc);
  client.setProvider({ rpc, signer });
  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const sender = await signer.publicKeyHash();

  // The user claims must be sorted by nonce at this point
  const contractUserClaims: object[] = request.userClaims.map((userClaim) => userClaimRequestObjectToSmartContractObject(userClaim));
  const contractAdminClaims: object[] = request.adminClaims.map((adminClaim) => adminClaimRequestObjectToSmartContractObject(adminClaim));
  const contractClaims = contractUserClaims.concat(contractAdminClaims);

  // Send the transaction
  let tx: TransactionOperation;
  try {
    tx = await contract.methods.register_tandem_claims(contractClaims).send();
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  // Respond to caller
  return new TransactionResponse(
    TransferStatus.SUCCESS,
    'transaction sent',
    sender,
    tx.hash,
    (tx.fee / parseFloat(tx.consumedGas)).toString(),
    tx.consumedGas,
    tx.gasLimit.toString()
  );
}