FROM node:18.17.0-alpine AS base

FROM base AS deps
WORKDIR /usr/app
COPY package.json package-lock.json ./
RUN mkdir -p /usr/app

FROM base AS builder
WORKDIR /usr/app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN yarn build

# Install dependencies based on the preferred package manager

FROM base AS runner
WORKDIR /app
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
COPY --from=builder /app/public ./public

EXPOSE 3000

CMD ["yarn", "start"] 