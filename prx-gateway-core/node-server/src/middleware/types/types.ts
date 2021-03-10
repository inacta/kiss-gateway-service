import { ContractAbstraction, ContractProvider } from '@taquito/taquito';
import BigNumber from 'bignumber.js';

export enum Status {
  ERROR = 'ERROR',
  SUCCESS = 'SUCCESS',
}

export enum TransferStatus {
  ERROR = 'ERROR',
  SUCCESS = 'SUCCESS',
}

export enum TokenType {
  FA1_2 = 'FA1_2',
  FA2 = 'FA2',
}

export enum WhitelistVersion {
  V0 = 'V0',
}

export interface IContractOriginationStatus {
  addressOfNewContract: string | undefined;
  clearCallback: () => void;
  hash: string | undefined;
  message: string;
  state: TransactionState;
}

export interface IPaymentStatus {
  clearCallback: () => void;
  hash: string | undefined;
  message: string;
  net: Net;
  state: TransactionState;
}

// Description of a Tezos smart contract
export interface IContractInformation {
  address: string;
  contract: ContractAbstraction<ContractProvider>;
  conversionFactor: BigNumber | undefined;
  decimals: number | undefined;
  symbol: string;
  tokenStandard: TokenStandard;
  methods: string[];
}

export interface ITokenMetadata {
  token_id: number;
  symbol: string;
  name: string;
  decimals: number;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  extras: any;
}

export enum Net {
  Mainnet = 'Mainnet',
  Testnet = 'Testnet',
}

export enum TransactionState {
  waiting = 'Waiting',
  failed = 'Failed',
  success = 'Success',
}

export enum TokenStandard {
  // eslint-disable-next-line @typescript-eslint/camelcase
  fa1_2 = 'fa1_2',
  fa2 = 'fa2',
  unknown = 'unknown',
}
