FROM node:10

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY package*.json ./

# RUN npm install
# If you are building your code for production
RUN npm ci --only=production

# Copy all other source code to work directory
ADD . /usr/src/app

# set environment to production
ENV NODE_ENV production

CMD [ "npm", "start" ]
EXPOSE 7000