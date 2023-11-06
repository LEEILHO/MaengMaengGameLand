# Stage 1: Build dependencies
FROM node:18.17.0-alpine AS deps
RUN apk add --no-cache libc6-compat
WORKDIR /usr/src/app
COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

# Stage 2: Build application
FROM deps AS builder
WORKDIR /usr/src/app
COPY . .
RUN yarn build

# Stage 3: Production image
FROM node:18.17.0-alpine AS production
WORKDIR /usr/src/app
RUN addgroup --system --gid 1001 nodejs && adduser --system --uid 1001 nextjs
COPY --from=builder /usr/src/app/package.json /usr/src/app/yarn.lock ./
COPY --from=builder /usr/src/app/.next ./.next
COPY --from=builder /usr/src/app/public ./public
ENV NODE_ENV production
USER nextjs
EXPOSE 3000
CMD ["yarn", "start"]
