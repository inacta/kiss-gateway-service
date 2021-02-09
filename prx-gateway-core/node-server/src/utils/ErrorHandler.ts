import { Response, NextFunction } from 'express'
import { HTTPClientError, HTTP404Error } from '../utils/httpErrors'

export const notFoundError = () => {
  throw new HTTP404Error(JSON.stringify('Method not found'))
}

export const clientError = (err: Error, res: Response, next: NextFunction) => {
  if (err instanceof HTTPClientError) {
    console.warn(err)
    res.status(err.statusCode).send(JSON.stringify(err.message))
  } else {
    next(err)
  }
}

export const serverError = (err: Error, res: Response, next: NextFunction) => {
  console.error(err)
  if (process.env.NODE_ENV === 'production') {
    res.status(500).send(JSON.stringify('Internal Server Error'))
  } else {
    res.status(500).send(JSON.stringify(err.stack))
  }
}
