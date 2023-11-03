FROM node:18.17.0-alpine AS base

FROM base AS deps
WORKDIR /usr/app
COPY package.json package-lock.json ./
RUN mkdir -p /usr/app

FROM base AS builder
WORKDIR /usr/app
COPY --from=deps /usr/app/node_modules ./node_modules
COPY . .
RUN yarn build

# Install dependencies based on the preferred package manager

FROM base AS runner
WORKDIR /app
COPY --from=builder /usr/app/.next/standalone ./
COPY --from=builder /usr/app/.next/static ./.next/static
COPY --from=builder /usr/app/public ./public

EXPOSE 3000

CMD ["yarn", "start"] 