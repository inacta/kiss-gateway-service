import { handleCors, handleBodyRequestParsing, handleCompression } from './requestHandling/common';

import { handleAPIDocs } from './misc/apiDocs';

export default [handleCors, handleBodyRequestParsing, handleCompression, handleAPIDocs];
