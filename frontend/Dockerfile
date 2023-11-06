FROM node:18.17.0-alpine

RUN mkdir -p /usr/app
WORKDIR /usr/app

RUN yarn install

# Install dependencies based on the preferred package manager
COPY ./ ./

RUN yarn build

EXPOSE 3000

CMD ["yarn", "start"]
