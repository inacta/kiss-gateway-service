import { Request, Response, NextFunction } from 'express';
import { HTTP400Error, HTTP401Error } from '../../utils/httpErrors';
const auth = require('basic-auth');
const md5 = require('md5');

export const checkQueryparamsExample = (req: Request, res: Response, next: NextFunction) => {
  if (!req.query.id) {
    throw new HTTP400Error('Missing id parameter');
  } else if (!req.query.url) {
    throw new HTTP400Error('Missing url parameter');
  } else {
    next();
  }
};
