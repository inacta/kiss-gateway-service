import { TransferStatus, Status } from './types';
import BigNumber from 'bignumber.js';

export class TransactionResponse {
  public status: TransferStatus;
  public message: string;
  public from: string | null;
  public txHash: string | null;
  public gasPrice: string | null;
  public gasUsed: string | null;
  public gasLimit: string | null;

  constructor(
    status: TransferStatus,
    message: string,
    from?: string,
    txHash?: string,
    gasPrice?: string,
    gasUsed?: string,
    gasLimit?: string
  ) {
    this.status = status;
    this.message = message;
    this.from = from || null;
    this.txHash = txHash || null;
    this.gasPrice = gasPrice || null;
    this.gasUsed = gasUsed || null;
    this.gasLimit = gasLimit || null;
  }
}

export class GetBalanceResponse {
  public balance: string;

  constructor(balance: string) {
    this.balance = balance;
  }
}

export class CheckAddressResponse {
  public valid: Boolean;

  constructor(valid: Boolean) {
    this.valid = valid;
  }
}

export class WhitelistResponse {
  public status: Status;
  public message: String;
  public whitelisted: Boolean;

  constructor(status: Status, message: String, valid: Boolean) {
    this.status = status;
    this.message = message;
    this.whitelisted = valid;
  }
}

export class KeyPair {
  public secretKey: String;
  public publicKey: String;
  public address: String;

  constructor(secretKey: String, publicKey: String, address: String) {
    this.secretKey = secretKey;
    this.publicKey = publicKey;
    this.address = address;
  }
}
