# Build stage
FROM node:18.17.0-alpine AS build

WORKDIR /usr/app
COPY ./ ./
RUN yarn install
RUN yarn build

# Final stage
FROM node:18.17.0-alpine

WORKDIR /usr/app
EXPOSE 3000
CMD ["yarn", "start"]