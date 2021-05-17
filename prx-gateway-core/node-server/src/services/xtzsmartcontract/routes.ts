import { Request, Response } from 'express';
import { calculateUserSignature, callAddAllowedActivity, callSuspendAllowedActivity, changeActivityLog, changeAdminThis, getActivityLogAddress, getAdmin, getAllowedActivities, getNonce, getSuspendedActivities, registerTandemClaims } from '../../middleware/lib/kiss';
import { handleTransfer, isValid, generateKeyPair, getWhitelist, modifyWhitelist, getBalance } from '../../middleware/lib/tezos';
import { GetBalanceResponse, TransactionResponse, WhitelistResponse } from '../../middleware/types/responseTypes';
import BigNumber from 'bignumber.js';

export default [
  {
    path: '/xtzsmartcontract/v1/ping',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        res.status(200).send('');
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/checkAddress',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const address: string = req.query.address as string;
        res.status(200).send(isValid(address));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getBalance',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        const address: string = req.query.address as string;
        const tokenId: BigNumber | undefined = req.query.tokenId ? new BigNumber(req.query.tokenId as string) : undefined;
        const response: GetBalanceResponse = await getBalance(contractAddress, address, tokenId);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/transfer',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await handleTransfer(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getWhitelist',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: WhitelistResponse = await getWhitelist(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/addToWhitelist',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await modifyWhitelist(req.body, true);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/removeFromWhitelist',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await modifyWhitelist(req.body, false);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getAdmin',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        console.log('input parameter: ' + contractAddress);
        res.status(200).send(await getAdmin(contractAddress));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getAllowedActivities',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        res.status(200).send(await getAllowedActivities(contractAddress));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getSuspendedActivities',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        res.status(200).send(await getSuspendedActivities(contractAddress));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getNonce',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        const address: string = req.query.address as string;
        res.status(200).send(await getNonce(contractAddress, address));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/getActivityLogAddress',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        const contractAddress: string = req.query.contractAddress as string;
        res.status(200).send(await getActivityLogAddress(contractAddress));
      },
    ],
  },
  // This does not change state but since it uses a secret key,
  // we want to keep it as a POST request since the secret key
  // may otherwise show up in logs and we don't want that.
  {
    path: '/xtzsmartcontract/v1/calculateUserSignature',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        res.status(200).send(await calculateUserSignature(req.body));
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/generateKeyPair',
    method: 'get',
    handler: [
      async (req: Request, res: Response) => {
        res.status(200).send(await generateKeyPair());
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/callSuspendAllowedActivity',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await callSuspendAllowedActivity(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/callAddAllowedActivity',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await callAddAllowedActivity(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/changeAdminThis',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await changeAdminThis(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/changeActivityLog',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await changeActivityLog(req.body);
        res.status(200).send(response);
      },
    ],
  },
  {
    path: '/xtzsmartcontract/v1/registerTandemClaims',
    method: 'post',
    handler: [
      async (req: Request, res: Response) => {
        const response: TransactionResponse = await registerTandemClaims(req.body);
        res.status(200).send(response);
      },
    ],
  },
];
