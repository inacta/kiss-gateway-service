import { ContractAbstraction, ContractProvider, TezosToolkit } from '@taquito/taquito';
import { IContractInformation, ITokenMetadata, TokenStandard } from '../../types/types';
import { ValidationResult, validateAddress } from '@taquito/utils';
import BigNumber from 'bignumber.js';

export const rpc = process.env.NODE_RPC_URI ?? 'https://tezos-florencenet.inacta.services';

// This file is to define helper functions for FA1.2 and FA2 token implementations

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function getObjectMethodNames(obj: any): string[] {
  if (!obj) {
    return [];
  }

  return Object.getOwnPropertyNames(obj)
    .filter((p) => typeof obj[p] === 'function')
    .map((name) => name.toLowerCase());
}

export const CONTRACT_ADDRESS_PREFIX = 'KT1';

// A valid contract address starts with 'KT1'
export function isContractAddress(address: string) {
  if (!address || address.length < 3) {
    return false;
  }

  return address.substring(0, 3) === CONTRACT_ADDRESS_PREFIX && validateAddress(address) === ValidationResult.VALID;
}

export function getContractInterface(contract: ContractAbstraction<ContractProvider>): [TokenStandard, string[]] {
  const methodNames: string[] = getObjectMethodNames(contract.methods);
  var standard = TokenStandard.unknown;
  if (
    // These function names are specified in FA2/TZIP-12
    ['transfer', 'balance_of', 'update_operators'].every((mn) => methodNames.includes(mn))
  ) {
    standard = TokenStandard.fa2;
  } else if (
    // These function names are specified in FA1.2/TZIP-7
    // The names with underscore are there for legacy reasons
    ['transfer', 'approve', 'getallowance', 'getbalance', 'gettotalsupply'].every((mn) => methodNames.includes(mn))
    || ['transfer', 'approve', 'get_allowance', 'get_balance', 'get_total_supply'].every((mn) => methodNames.includes(mn))
  ) {
    standard = TokenStandard.fa1_2;
  }

  return [standard, methodNames];
}

// Given a contract, fetch information about it from the blockchain and return
// an object describing the deployed token contract
export function getContractInformation(contract: ContractAbstraction<ContractProvider>): Promise<IContractInformation> {
  const info = getContractInterface(contract);
  if (info && info[0] === TokenStandard.fa2) {
    return (
      contract
        .storage()
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        .then((s: any) => s.token_metadata.get('0'))
        .then((md: ITokenMetadata) => {
          return {
            address: contract.address,
            contract,
            conversionFactor: new BigNumber(10).pow(md?.decimals ?? new BigNumber(0)),
            decimals: md.decimals,
            methods: info[1],
            symbol: md.symbol,
            tokenStandard: info[0]
          };
        })
    );
  } else if (info && info[0] === TokenStandard.fa1_2) {
    return Promise.resolve({
      address: contract.address,
      contract,
      conversionFactor: new BigNumber(1),
      decimals: 0,
      methods: info[1],
      symbol: 'Unknown',
      tokenStandard: info[0]
    });
  }

  return Promise.resolve({
    address: contract.address,
    contract,
    conversionFactor: undefined,
    decimals: undefined,
    methods: info[1],
    symbol: 'Unknown',
    tokenStandard: info[0]
  });
}

export function getSymbol(info: IContractInformation): string {
  if (!info || info.tokenStandard !== TokenStandard.fa2) {
    return 'tokens';
  } else {
    return info.symbol;
  }
}

export function getTokenBalance(
  tokenAddress: string,
  accountAddress: string,
  client: TezosToolkit,
  tokenId?: BigNumber | undefined
): Promise<BigNumber> {
  return new Promise((resolve, reject) => {
    if (!isContractAddress(tokenAddress)) {
      reject(`Invalid token address: ${tokenAddress}`);
    }
    if (accountAddress === '' || validateAddress(accountAddress) !== ValidationResult.VALID) {
      reject(`Invalid account address: ${accountAddress}`);
    }

    var conversionFactor = new BigNumber(1);
    client.contract.at(tokenAddress).then((c) => {
      getContractInformation(c).then((ci) => {

        ci.contract.storage()
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          .then((s: any) => {
            if (s.token_metadata) {
              // eslint-disable-next-line @typescript-eslint/no-explicit-any
              s.token_metadata.get(tokenId?.toString() ?? '0').then((md: any) => {
                conversionFactor = new BigNumber(10).pow(md?.decimals ?? 0);
              });
            }

            return (s.ledger || s.balances).get(accountAddress);
          })
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          .then((acc: any) => {
            switch (ci.tokenStandard) {
              case TokenStandard.fa1_2:
                resolve(
                  acc?.balance === undefined ? new BigNumber(0) : new BigNumber(acc.balance).dividedBy(conversionFactor)
                )
                break;
              case TokenStandard.fa2:
                const balances: any = acc?.balances;
                if (!balances || !balances.get(tokenId?.toString()))
                  return resolve(new BigNumber(0));
                return resolve(balances.get(tokenId?.toString()).dividedBy(conversionFactor))
              default:
                reject(`Unimplemented token standard. Got: ${ci.tokenStandard.toString()}`);
            }
          })
      })
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .catch((err: any) => reject(err));
    });
  });
}
