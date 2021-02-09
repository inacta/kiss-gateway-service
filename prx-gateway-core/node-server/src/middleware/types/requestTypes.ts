import { TokenType, WhitelistVersion } from './types';
import BigNumber from 'bignumber.js';

export interface TokenTransferRequest {
  contractAddress: string;
  to: string;
  amount: string;
  tokenId: string;
  secretKey: string;
}

export interface GetWhitelistRequest {
  version: WhitelistVersion;
  contractAddress: string;
  walletAddress: string;
  tokenId: string;
}

export interface ModifyWhitelistRequest extends GetWhitelistRequest {
  secretKey: string;
}

export interface KissTandemClaimForSmartContract {
  helpers: string[];
  activities: number[];
  minutes: number;
  helpees: string[];
}
