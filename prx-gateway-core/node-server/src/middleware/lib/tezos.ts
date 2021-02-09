import { TezosToolkit, ContractAbstraction, ContractProvider } from '@taquito/taquito';
import { validateAddress, ValidationResult } from '@taquito/utils';
import { InMemorySigner } from '@taquito/signer';
import { getContractInformation, isContractAddress, rpc } from './shared/tokenImplemenation';
import { TokenTransferRequest, GetWhitelistRequest, ModifyWhitelistRequest } from '../types/requestTypes';
import { TransactionResponse, CheckAddressResponse, WhitelistResponse } from '../types/responseTypes';
import { TokenStandard, TokenType, TransferStatus, Status, WhitelistVersion } from '../types/types';
import BigNumber from 'bignumber.js';
import { TransactionOperation } from '@taquito/taquito/dist/types/operations/transaction-operation';

export function isValid(address: string): CheckAddressResponse {
  return new CheckAddressResponse(validateAddress(address) === ValidationResult.VALID);
}

// Return a boolean indicating if address is whitelisted or not, internal
async function isWhitelistedInternal(
  walletAddress: string,
  contractAddress: string,
  client: TezosToolkit
): Promise<boolean> {
  const contract: any = await client.contract.at(contractAddress);
  return contract.storage().then((s: any) => s.whitelisteds.includes(walletAddress));
}

// Return a boolean indicating if address is whitelisted or not
export async function getWhitelist(request: GetWhitelistRequest): Promise<WhitelistResponse> {
  if (request.version === WhitelistVersion.V0) {
    const client = new TezosToolkit();
    client.setProvider({ rpc });
    return new WhitelistResponse(
      Status.SUCCESS,
      '',
      await isWhitelistedInternal(request.walletAddress, request.contractAddress, client)
    );
  } else {
    return new WhitelistResponse(Status.ERROR, 'Whitelist version not supported', false);
  }
}

// Add or remove address from whitelist
export async function modifyWhitelist(request: ModifyWhitelistRequest, add: boolean): Promise<TransactionResponse> {
  if (request.version === WhitelistVersion.V0) {
    // input validation
    if (!isContractAddress(request.contractAddress)) {
      return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
    }
    if (validateAddress(request.walletAddress) !== ValidationResult.VALID) {
      return new TransactionResponse(TransferStatus.ERROR, 'Provided target address is invalid');
    }

    const client = new TezosToolkit();
    let signer;
    try {
      signer = new InMemorySigner(request.secretKey);
    } catch (e) {
      return new TransactionResponse(TransferStatus.ERROR, 'Provided secret key is invalid');
    }
    client.setProvider({ rpc, signer });

    const isWhitelisted = await isWhitelistedInternal(request.walletAddress, request.contractAddress, client);
    if (isWhitelisted === add) {
      const msg: string = `Address is already ${isWhitelisted ? 'whitelisted' : 'not whitelisted'}`;
      return new TransactionResponse(TransferStatus.ERROR, msg);
    }

    const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
    let whitelistParam;
    if (add) {
      whitelistParam = [
        {
          add_whitelisted: request.walletAddress,
        },
      ];
    } else {
      whitelistParam = [
        {
          remove_whitelisted: request.walletAddress,
        },
      ];
    }

    let tx: TransactionOperation;
    try {
      tx = await contract.methods.update_whitelisteds(whitelistParam).send();
    } catch (error) {
      return new TransactionResponse(TransferStatus.ERROR, error.message);
    }

    const sender = await signer.publicKeyHash();
    return new TransactionResponse(
      TransferStatus.SUCCESS,
      'transaction sent',
      sender,
      tx.hash,
      (tx.fee / parseFloat(tx.consumedGas)).toString(),
      tx.consumedGas,
      tx.gasLimit.toString()
    );
  } else {
    return new TransactionResponse(TransferStatus.ERROR, 'Whitelist version not supported');
  }
}

export async function handleTransfer(request: TokenTransferRequest): Promise<TransactionResponse> {
  if (!isContractAddress(request.contractAddress)) {
    return new TransactionResponse(TransferStatus.ERROR, 'No valid contract address provided');
  }
  if (validateAddress(request.to) !== ValidationResult.VALID) {
    return new TransactionResponse(TransferStatus.ERROR, 'Provided target address is invalid');
  }
  let signer;
  try {
    signer = new InMemorySigner(request.secretKey);
  } catch (e) {
    return new TransactionResponse(TransferStatus.ERROR, e.message);
  }

  const client = new TezosToolkit();
  client.setProvider({ rpc, signer });

  const contract: ContractAbstraction<ContractProvider> = await client.contract.at(request.contractAddress);
  const tokenInfo = await getContractInformation(contract);
  const sender = await signer.publicKeyHash();

  let tx: TransactionOperation;
  switch (tokenInfo.tokenStandard) {
    case TokenStandard.fa1_2:
      try {
        tx = await contract.methods.transfer(sender, request.to, parseInt(request.amount)).send();
      } catch (e) {
        return new TransactionResponse(TransferStatus.ERROR, e.message);
      }
      break;
    case TokenStandard.fa2:
      const transferParam = [
        {
          from_: sender,
          txs: [
            {
              amount: new BigNumber(request.amount).multipliedBy(
                new BigNumber(10).pow(new BigNumber(tokenInfo.decimals as number))
              ),
              to_: request.to,
              token_id: parseInt(request.tokenId),
            },
          ],
        },
      ];
      try {
        tx = await contract.methods.transfer(transferParam).send();
      } catch (e) {
        return new TransactionResponse(TransferStatus.ERROR, e.message);
      }
      break;
    default:
      return new TransactionResponse(TransferStatus.ERROR, 'Unsupported Token Type');
  }

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
