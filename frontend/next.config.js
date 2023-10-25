/** @type {import('next').NextConfig} */
const nextConfig = {
  compiler: {
    styledComponents: true,
  },
}

const withPWA = require('next-pwa')({
  disable: process.env.NODE_ENV === 'development',
  customWorkerDir: './worker',
  dest: 'public',
  register: true,
  skipWaiting: true,
  runtimeCaching: require('next-pwa/cache'),
  buildExcludes: [/app-build-manifest.json$/],
})

module.exports = withPWA(nextConfig)
