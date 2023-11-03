FROM node:18.17.0-alpine

RUN mkdir -p /usr/app
WORKDIR /usr/app

# Install dependencies based on the preferred package manager
COPY ./ ./

EXPOSE 3000

CMD ["yarn", "start"]