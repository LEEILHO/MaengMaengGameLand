/** @type {import('next').NextConfig} */
const nextConfig = {
  compiler: {
    styledComponents: true,
  },
  reactStrictMode: false,
}

const withPWA = require('next-pwa')({
  customWorkerDir: 'src/worker',
  dest: 'public',
})

module.exports = withPWA(nextConfig)
