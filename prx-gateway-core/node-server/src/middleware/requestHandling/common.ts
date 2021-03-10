import { Request, Response, NextFunction, Router } from 'express';
import cors from 'cors'
import parser from 'body-parser'
import compression from 'compression'

export const handleCors = (router: Router) =>
  router.use(cors({ credentials: true, origin: true }))

export const handleBodyRequestParsing = (router: Router) => {
  router.use(parser.urlencoded({ extended: true }))
  router.use(parser.json())
}

export const handleCompression = (router: Router) => {
  router.use(compression())
}

const handleServerError = (router: Router) => {
  router.use((req: Request, res: Response, next: NextFunction) => {
    console.log(req.baseUrl);
    console.log(req.url);
    console.log(req.route);
  });
};
